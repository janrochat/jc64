from projectfile import ProjectFile
import sys

if len(sys.argv) < 2:
    print("Usage: read_project.py <project.dis>")
    sys.exit(1)

proj = ProjectFile.load(sys.argv[1])
print(f"Name: {proj.name}")
print(f"File: {proj.file}")
print(f"Description: {proj.description}")
print(f"File type: {proj.file_type}")
print(f"Target: {proj.target_type}")
print(f"Memory entries: {len(proj.memory)}")
