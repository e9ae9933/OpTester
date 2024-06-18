ldc esp 128
ldc ebp 128

ldc eax 712903490
ldc ebx 729813470
enter
push eax
push ebx
call g
pop edx

halt

# g
ldstk eax 0
ldstk ebx 4
jz f0 ebx
mod edx eax ebx
enter
push ebx
push edx
call g
pop ecx
ret ecx
# f0
ret eax