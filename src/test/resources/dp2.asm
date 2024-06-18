
ldc esp 256
ldc ebp 256

ldc eax 114514

enter
push eax
call label
pop ecx
jz fail ecx
halt

# label
pop ebx
jz fail eax
ldc ebx 1919810
ret ebx


# fail
halt