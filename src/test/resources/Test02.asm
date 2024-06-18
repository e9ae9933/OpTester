ldc esp 128
ldc ebp 128

enter
ldc eax 1
push eax
ldc eax 2
push eax
call dfs
pop edx
halt
# dfs
ldstk eax 0
ldstk ebx 4
ldc ecx 100000
lss edx eax ecx
jz label edx
add eax eax ebx
enter
push ebx
push eax
call dfs
pop eax
# label
ret eax