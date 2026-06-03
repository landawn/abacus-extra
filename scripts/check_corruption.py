"""Check for signs of file corruption/decompilation across the source classes."""
import os
import re
import sys

base = sys.argv[1] if len(sys.argv) > 1 else 'src/main/java'

# Signs of decompiled class files
decomp_artifacts = ['// $FF:', 'synthetic method', 'bridge method', 'decompiled', '// compiled from']

for root, dirs, fnames in os.walk(base):
    for f in sorted(fnames):
        if not f.endswith('.java'):
            continue
        fpath = os.path.join(root, f)
        with open(fpath, 'r', encoding='utf-8', errors='ignore') as fh:
            content = fh.read()
        
        issues = []
        for a in decomp_artifacts:
            if a in content:
                issues.append(a)
        
        if issues:
            print(f'CORRUPTED: {f} ({", ".join(issues)})')
        
        # Also check for missing field javadoc
        lines = content.split('\n')
        bare_fields = [l.strip() for l in lines if re.match(r'^\s*private static final ', l) and '//' not in l and '/**' not in '']
        
        # Check javadoc count
        javadoc_count = content.count('/**')
        usage_count = content.count('<p><b>Usage Examples')
        
        if javadoc_count == 0 and usage_count == 0:
            continue  # no javadoc at all, skip
        
        print(f'  {f}: {len(lines)} lines, javadoc={javadoc_count}, usage_examples={usage_count}')
