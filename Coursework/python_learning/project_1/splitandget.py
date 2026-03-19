f = open("an_essay.md","r",encoding="utf-8")
s = open("split.md","w")
g = open("get.md","w")

for line in f :
    line =line.strip()
    for part in line.split(".") :
        if part != "" :
            part = part.strip()
            s.write(part + ".\n")

s.flush()
s.close()
f.close()

s = open("split.md","r")

for line in s.readlines():
    line = line.strip()
    if "I" in line.split(" ") :
        g.write(line+"\n")

g.flush()
g.close()
s.close()
