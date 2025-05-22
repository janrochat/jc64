import gzip
import struct
from dataclasses import dataclass, field
from typing import BinaryIO, Dict, List, Optional, Tuple


# Constants taken from the Java implementation
MIN_COLS = 10
MIN_ROWS = 256
COLS = 20
ROWS = 0x10000  # 65536


def _read(fmt: str, f: BinaryIO):
    size = struct.calcsize(fmt)
    data = f.read(size)
    return struct.unpack(fmt, data)[0]


def _write(fmt: str, f: BinaryIO, value):
    f.write(struct.pack(fmt, value))


def read_boolean(f: BinaryIO) -> bool:
    return _read('>?', f)


def write_boolean(f: BinaryIO, v: bool):
    _write('>?', f, v)


def read_byte(f: BinaryIO) -> int:
    return _read('>b', f)


def write_byte(f: BinaryIO, v: int):
    _write('>b', f, v)


def read_ubyte(f: BinaryIO) -> int:
    return _read('>B', f)


def write_ubyte(f: BinaryIO, v: int):
    _write('>B', f, v)


def read_int(f: BinaryIO) -> int:
    return _read('>i', f)


def write_int(f: BinaryIO, v: int):
    _write('>i', f, v)


def read_char(f: BinaryIO) -> str:
    return chr(_read('>H', f))


def write_char(f: BinaryIO, c: str):
    _write('>H', f, ord(c))


def read_utf(f: BinaryIO) -> str:
    length = _read('>H', f)
    data = f.read(length)
    return data.decode('utf-8')


def write_utf(f: BinaryIO, s: str):
    data = s.encode('utf-8')
    _write('>H', f, len(data))
    f.write(data)


@dataclass
class MemoryEntry:
    address: int
    dasm_comment: Optional[str] = None
    user_comment: Optional[str] = None
    user_block_comment: Optional[str] = None
    dasm_location: Optional[str] = None
    user_location: Optional[str] = None
    is_inside: bool = False
    is_code: bool = False
    is_data: bool = False
    is_garbage: bool = False
    data_type: str = 'NONE'
    copy: int = 0
    related: int = 0
    type: str = ' '
    index: int = 0
    related_address_base: int = 0
    related_address_dest: int = 0
    basic_type: str = 'NONE'


@dataclass
class Relocate:
    from_start: int
    from_end: int
    to_start: int
    to_end: int


@dataclass
class Patch:
    address: int
    value: int


@dataclass
class Freeze:
    name: str
    text: str


@dataclass
class ProjectFile:
    version: int = 11
    name: str = ''
    file: str = ''
    description: str = ''
    file_type: str = 'UND'
    target_type: str = 'C64'
    inB: bytes = b''
    memory_flags: bytes = b''
    memory: List[MemoryEntry] = field(default_factory=list)
    chip: int = 0
    constant_table: Dict[Tuple[int, int], str] = field(default_factory=dict)
    constant_comment: Dict[Tuple[int, int], str] = field(default_factory=dict)
    relocates: List[Relocate] = field(default_factory=list)
    patches: List[Patch] = field(default_factory=list)
    freezes: List[Freeze] = field(default_factory=list)
    bin_address: int = 0

    @staticmethod
    def _is_gzipped(path: str) -> bool:
        with open(path, 'rb') as f:
            magic = f.read(2)
            return magic == b'\x1f\x8b'

    @classmethod
    def load(cls, path: str) -> 'ProjectFile':
        pf = cls()
        gz = cls._is_gzipped(path)
        opener = gzip.open if gz else open
        with opener(path, 'rb') as f:
            pf.version = read_ubyte(f)
            pf.name = read_utf(f)
            pf.file = read_utf(f)
            pf.description = read_utf(f)
            pf.file_type = read_utf(f)
            pf.target_type = read_utf(f) if pf.version > 0 else 'C64'

            size = read_int(f)
            pf.inB = f.read(size)

            size = read_int(f)
            pf.memory_flags = f.read(size)

            size = read_int(f)
            for _ in range(size):
                mem = MemoryEntry(address=read_int(f))
                if read_boolean(f):
                    mem.dasm_comment = read_utf(f)
                if read_boolean(f):
                    mem.user_comment = read_utf(f)
                if read_boolean(f):
                    mem.user_block_comment = read_utf(f)
                if read_boolean(f):
                    mem.dasm_location = read_utf(f)
                if read_boolean(f):
                    mem.user_location = read_utf(f)
                mem.is_inside = read_boolean(f)
                mem.is_code = read_boolean(f)
                mem.is_data = read_boolean(f)
                if pf.version > 0:
                    mem.is_garbage = read_boolean(f)
                    mem.data_type = read_utf(f)
                mem.copy = read_ubyte(f)
                mem.related = read_int(f)
                mem.type = read_char(f)
                if pf.version > 2:
                    mem.index = read_ubyte(f)
                if pf.version > 8:
                    mem.related_address_base = read_int(f)
                    mem.related_address_dest = read_int(f)
                if pf.version > 9:
                    mem.basic_type = read_utf(f)
                pf.memory.append(mem)

            if pf.version > 1:
                pf.chip = read_int(f)

            if pf.version > 2:
                for i in range(MIN_COLS):
                    for j in range(MIN_ROWS):
                        if read_boolean(f):
                            pf.constant_table[(i, j)] = read_utf(f)

            if pf.version > 3:
                size = read_int(f)
                for _ in range(size):
                    pf.relocates.append(
                        Relocate(
                            read_int(f),
                            read_int(f),
                            read_int(f),
                            read_int(f)
                        )
                    )

            if pf.version > 4:
                size = read_int(f)
                for _ in range(size):
                    pf.patches.append(Patch(read_int(f), read_int(f)))

            if pf.version > 5:
                for i in range(MIN_COLS):
                    for j in range(MIN_ROWS, ROWS):
                        if read_boolean(f):
                            pf.constant_table[(i, j)] = read_utf(f)

            if pf.version > 6:
                size = read_int(f)
                for _ in range(size):
                    name = read_utf(f)
                    if read_boolean(f):
                        size2 = read_int(f)
                        text = f.read(size2).decode('utf-8')
                    else:
                        text = read_utf(f)
                    pf.freezes.append(Freeze(name, text))

            if pf.version > 7:
                for i in range(MIN_COLS, COLS):
                    for j in range(ROWS):
                        if read_boolean(f):
                            pf.constant_table[(i, j)] = read_utf(f)

            if pf.version > 9:
                for i in range(COLS):
                    for j in range(ROWS):
                        if read_boolean(f):
                            pf.constant_comment[(i, j)] = read_utf(f)

            if pf.version > 10:
                pf.bin_address = read_int(f)

        return pf

    def save(self, path: str, gzipped: bool = True):
        opener = gzip.open if gzipped else open
        with opener(path, 'wb') as f:
            write_ubyte(f, self.version)
            write_utf(f, self.name)
            write_utf(f, self.file)
            write_utf(f, self.description)
            write_utf(f, self.file_type)
            write_utf(f, self.target_type)

            write_int(f, len(self.inB))
            f.write(self.inB)

            write_int(f, len(self.memory_flags))
            f.write(self.memory_flags)

            write_int(f, len(self.memory))
            for mem in self.memory:
                write_int(f, mem.address)
                if mem.dasm_comment is not None:
                    write_boolean(f, True)
                    write_utf(f, mem.dasm_comment)
                else:
                    write_boolean(f, False)

                if mem.user_comment is not None:
                    write_boolean(f, True)
                    write_utf(f, mem.user_comment)
                else:
                    write_boolean(f, False)

                if mem.user_block_comment is not None:
                    write_boolean(f, True)
                    write_utf(f, mem.user_block_comment)
                else:
                    write_boolean(f, False)

                if mem.dasm_location is not None:
                    write_boolean(f, True)
                    write_utf(f, mem.dasm_location)
                else:
                    write_boolean(f, False)

                if mem.user_location is not None:
                    write_boolean(f, True)
                    write_utf(f, mem.user_location)
                else:
                    write_boolean(f, False)

                write_boolean(f, mem.is_inside)
                write_boolean(f, mem.is_code)
                write_boolean(f, mem.is_data)
                write_boolean(f, mem.is_garbage)
                write_utf(f, mem.data_type)
                write_ubyte(f, mem.copy)
                write_int(f, mem.related)
                write_char(f, mem.type)
                write_ubyte(f, mem.index)
                write_int(f, mem.related_address_base)
                write_int(f, mem.related_address_dest)
                write_utf(f, mem.basic_type)

            write_int(f, self.chip)

            for i in range(MIN_COLS):
                for j in range(MIN_ROWS):
                    val = self.constant_table.get((i, j))
                    if val is not None:
                        write_boolean(f, True)
                        write_utf(f, val)
                    else:
                        write_boolean(f, False)

            write_int(f, len(self.relocates))
            for r in self.relocates:
                write_int(f, r.from_start)
                write_int(f, r.from_end)
                write_int(f, r.to_start)
                write_int(f, r.to_end)

            write_int(f, len(self.patches))
            for p in self.patches:
                write_int(f, p.address)
                write_int(f, p.value)

            for i in range(MIN_COLS):
                for j in range(MIN_ROWS, ROWS):
                    val = self.constant_table.get((i, j))
                    if val is not None:
                        write_boolean(f, True)
                        write_utf(f, val)
                    else:
                        write_boolean(f, False)

            write_int(f, len(self.freezes))
            for fr in self.freezes:
                write_utf(f, fr.name)
                data = fr.text.encode('utf-8')
                if len(data) > 0xFFFF:
                    write_boolean(f, True)
                    write_int(f, len(data))
                    f.write(data)
                else:
                    write_boolean(f, False)
                    write_utf(f, fr.text)

            for i in range(MIN_COLS, COLS):
                for j in range(ROWS):
                    val = self.constant_table.get((i, j))
                    if val is not None:
                        write_boolean(f, True)
                        write_utf(f, val)
                    else:
                        write_boolean(f, False)

            for i in range(COLS):
                for j in range(ROWS):
                    val = self.constant_comment.get((i, j))
                    if val is not None:
                        write_boolean(f, True)
                        write_utf(f, val)
                    else:
                        write_boolean(f, False)

            write_int(f, self.bin_address)



