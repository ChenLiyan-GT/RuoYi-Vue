import pathlib, re
root = pathlib.Path(r'd:/giencoder/RuoYi-Vue/ruoyi-admin/.gientech/wiki')
text = (root / '目录.md').read_text(encoding='utf-8')
links = re.findall(r'\[.*?\]\((.*?)\)', text)
print('LINKS:')
missing = []
for link in links:
    path = root / link
    if path.exists():
        print('OK:', link)
    else:
        missing.append(link)
for link in missing:
    print('MISSING:', link)
print('FILES:')
for p in sorted(root.rglob('*')):
    print(str(p.relative_to(root)).replace('\\', '/'))
