import sys
import json
import base64
from dataclasses import asdict

from projectfile import (
    ProjectFile,
    MemoryEntry,
    Relocate,
    Patch,
    Freeze,
)


def project_to_dict(pf: ProjectFile) -> dict:
    """Convert a ProjectFile instance to a JSON serialisable dictionary."""
    data = asdict(pf)
    data["inB"] = base64.b64encode(pf.inB).decode("ascii")
    data["memory_flags"] = base64.b64encode(pf.memory_flags).decode("ascii")
    data["constant_table"] = {
        f"{k[0]},{k[1]}": v for k, v in pf.constant_table.items()
    }
    data["constant_comment"] = {
        f"{k[0]},{k[1]}": v for k, v in pf.constant_comment.items()
    }
    return data


def dict_to_project(data: dict) -> ProjectFile:
    """Create a ProjectFile instance from a dictionary."""
    pf = ProjectFile()
    pf.version = data.get("version", pf.version)
    pf.name = data.get("name", "")
    pf.file = data.get("file", "")
    pf.description = data.get("description", "")
    pf.file_type = data.get("file_type", "UND")
    pf.target_type = data.get("target_type", "C64")
    pf.inB = base64.b64decode(data.get("inB", ""))
    pf.memory_flags = base64.b64decode(data.get("memory_flags", ""))
    pf.memory = [MemoryEntry(**m) for m in data.get("memory", [])]
    pf.chip = data.get("chip", 0)
    pf.constant_table = {
        tuple(map(int, k.split(","))): v
        for k, v in data.get("constant_table", {}).items()
    }
    pf.constant_comment = {
        tuple(map(int, k.split(","))): v
        for k, v in data.get("constant_comment", {}).items()
    }
    pf.relocates = [Relocate(**r) for r in data.get("relocates", [])]
    pf.patches = [Patch(**p) for p in data.get("patches", [])]
    pf.freezes = [Freeze(**f) for f in data.get("freezes", [])]
    pf.bin_address = data.get("bin_address", 0)
    return pf


if __name__ == "__main__":
    if len(sys.argv) < 4:
        print(
            "Usage: convert_project_json.py <input.dis> <output.json> <restored.dis>"
        )
        sys.exit(1)

    dis_in = sys.argv[1]
    json_out = sys.argv[2]
    dis_out = sys.argv[3]

    # Read project and convert to JSON
    project = ProjectFile.load(dis_in)
    with open(json_out, "w", encoding="utf-8") as f:
        json.dump(project_to_dict(project), f, indent=2)
    print(f"Project written to {json_out}")

    # Read JSON back and write a new project file
    with open(json_out, "r", encoding="utf-8") as f:
        data = json.load(f)
    restored = dict_to_project(data)
    restored.save(dis_out, gzipped=True)
    print(f"Project restored to {dis_out}")
