ldc esp 256
ldc ebp 256

ldc eax 1
ldc ebx 1

equ edx eax ebx
push edx
neq edx eax ebx
push edx
lss edx eax ebx
push edx
geq edx eax ebx
push edx
gtr edx eax ebx
push edx
leq edx eax ebx
push edx

halt