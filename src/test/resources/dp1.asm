
ldc esp 256
ldc ebp 256

ldc eax 0
ldc ebx 1061109567
push ebx
push eax
ldstk ecx 1
movreg edx ecx
push edx
ststk 32 ecx

ldc eax 258
ldreg ebx eax
ldc ecx 500
streg ecx ebx

halt