from projectfile import ProjectFile, MemoryEntry

# Create a minimal project with a single memory entry
proj = ProjectFile()
proj.name = "demo"
proj.file = "demo.bin"
proj.description = "Example project"
proj.file_type = "UND"
proj.target_type = "C64"

# Example binary data
proj.inB = b"\x00\x01\x02"
proj.memory_flags = b"\x00\x00\x00"

# Add one memory entry
mem = MemoryEntry(address=0)
mem.is_inside = True
proj.memory.append(mem)

# Save gzipped project file
proj.save("demo.dis", gzipped=True)
print("Project written to demo.dis")
