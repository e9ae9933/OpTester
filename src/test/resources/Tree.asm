ldc esp 2048
ldc ebp 2048
add esp esp esp
add ebp ebp ebp

ldc ebx 16384
movr 2048 ebx


enter
ldc ebx 24
push ebx
call new

pop eax
push eax

enter
push eax
ldc ebx 1
push ebx
input ebx
push ebx
call build

# loop

input eax
ldc ebx 1
equ ecx ebx eax
jnz l1 ecx
ldc ebx 2
equ ecx ebx eax
jnz l2 ecx
halt

# l1

ldstk eax 0
input ebx
input ecx
input edx
enter
push eax
push ebx
push ecx
push edx
call add
jmp loop

# l2

ldstk eax 0
input ebx
input ecx
enter
push eax
push ebx
push ecx
call query

pop edx
output edx
jmp loop



# new
    ldstk eax 0
    mov ebx 2048
    add ecx ebx eax
    movr 2048 ecx
    ret ebx

# build
    ldstk eax 0
    ldc ebx 0
    add eax eax ebx
    ldstk edx 4
    streg eax edx

    ldstk eax 0
    ldc ebx 4
    add eax eax ebx
    ldstk edx 8
    streg eax edx

    ldstk eax 0
    ldc ebx 16
    add eax eax ebx
    ldc edx 0
    streg eax edx

    ldstk eax 0
    ldc ebx 20
    add eax eax ebx
    ldc edx 0
    streg eax edx

    ldstk ebx 4
    ldstk ecx 8
    lss edx ebx ecx
    jnz cont edx
    leave
    # cont
        add eax ebx ecx
        ldc edx 2
        div eax eax edx
        push eax
            ldc edx 24
            enter
            push edx
            call new
            pop edx
        ldstk eax 0
        ldc ebx 8
        add eax eax ebx
        streg eax edx
            ldstk eax 4
            ldstk ebx 12
            enter
            push edx
            push eax
            push ebx
            call build

            ldc edx 24
            enter
            push edx
            call new
            pop edx
        ldstk eax 0
        ldc ebx 12
        add eax eax ebx
        streg eax edx
            ldstk eax 12
            ldc ebx 1
            add eax eax ebx
            ldstk ebx 8
            enter
            push edx
            push eax
            push ebx
            call build

        leave

# add
    ldstk eax 0
    ldc ebx 0
    add eax eax ebx
    ldreg eax eax
    ldstk ebx 8
    gtr edx eax ebx
    jz add1 edx
    leave
    # add1
    ldstk eax 0
    ldc ebx 4
    add eax eax ebx
    ldreg eax eax
    ldstk ebx 4
    lss edx eax ebx
    jz add2 edx
    leave
    # add2
    ldstk eax 0
    ldc ebx 0
    add eax eax ebx
    ldreg eax eax
    ldstk ebx 4
    geq edx eax ebx
    jz add3 edx
    ldstk eax 0
    ldc ebx 4
    add eax eax ebx
    ldreg eax eax
    ldstk ebx 8
    leq edx eax ebx
    jz add3 edx

        ldstk ecx 0
        ldstk edx 12
        enter
        push ecx
        push edx
        call addlazy
        leave
    # add3
        ldstk eax 0
        ldc ebx 8
        add eax eax ebx
        ldreg eax eax
        ldstk ebx 4
        ldstk ecx 8
        ldstk edx 12
        enter
        push eax
        push ebx
        push ecx
        push edx
        call add

        ldstk eax 0
        ldc ebx 12
        add eax eax ebx
        ldreg eax eax
        ldstk ebx 4
        ldstk ecx 8
        ldstk edx 12
        enter
        push eax
        push ebx
        push ecx
        push edx
        call add

            ldstk eax 0
            enter
            push eax
            call pushup
    leave


# pushup
    ldstk eax 0
    ldc edx 0

    ldc ebx 8
    add ebx eax ebx
    ldreg ebx ebx
    ldc ecx 16
    add ebx ebx ecx
    ldreg ebx ebx
    add edx ebx edx

    ldc ebx 12
    add ebx eax ebx
    ldreg ebx ebx
    ldc ecx 16
    add ebx ebx ecx
    ldreg ebx ebx
    add edx ebx edx

    ldc ebx 16
    add ebx eax ebx
    streg ebx edx

    leave

# addlazy
    ldstk eax 0
    ldc ebx 4
    add eax eax ebx
    ldreg ecx eax

    ldstk eax 0
    ldc ebx 0
    add eax eax ebx
    ldreg edx eax

    sub edx ecx edx
    ldc ecx 1
    add edx edx ecx

    ldstk ebx 4
    mul edx edx ebx

    ldstk eax 0
    ldc ebx 16
    add eax eax ebx
    ldreg ecx eax
    add edx edx ecx
    streg eax edx

    ldstk eax 0
    ldc ebx 20
    add eax eax ebx
    ldreg edx eax
    ldstk ebx 4
    add edx edx ebx
    streg eax edx

    leave

# pushdown
    ldstk eax 0
    ldc ebx 20
    add eax ebx eax
    ldreg ecx eax

    ldstk eax 0
    ldc ebx 8
    add eax ebx eax
    ldreg edx eax

        enter
        push edx
        push ecx
        call addlazy

    ldstk eax 0
    ldc ebx 20
    add eax ebx eax
    ldreg ecx eax

    ldstk eax 0
    ldc ebx 12
    add eax ebx eax
    ldreg edx eax

        enter
        push edx
        push ecx
        call addlazy

    ldstk eax 0
    ldc ebx 20
    add eax ebx eax
    ldc edx 0
    streg eax edx

    leave



# query
    ldstk eax 0
    ldc ebx 0
    add eax eax ebx
    ldreg eax eax
    ldstk ebx 8
    gtr edx eax ebx
    jz query1 edx
    xor edx edx edx
    ret edx
    # query1
    ldstk eax 0
    ldc ebx 4
    add eax eax ebx
    ldreg eax eax
    ldstk ebx 4
    lss edx eax ebx
    jz query2 edx
    xor edx edx edx
    ret edx
    # query2
    ldstk eax 0
    ldc ebx 0
    add eax eax ebx
    ldreg eax eax
    ldstk ebx 4
    geq edx eax ebx
    jz query3 edx
    ldstk eax 0
    ldc ebx 4
    add eax eax ebx
    ldreg eax eax
    ldstk ebx 8
    leq edx eax ebx
    jz query3 edx

        ldstk eax 0
        ldc ebx 16
        add eax eax ebx
        ldreg edx eax
        ret edx
    # query3

        ldstk ecx 0
        enter
        push ecx
        call pushdown



        ldstk eax 0
        ldc ebx 8
        add eax eax ebx
        ldreg eax eax
        ldstk ebx 4
        ldstk ecx 8
        enter
        push eax
        push ebx
        push ecx
        call query

        ldstk eax 0
        ldc ebx 12
        add eax eax ebx
        ldreg eax eax
        ldstk ebx 4
        ldstk ecx 8
        enter
        push eax
        push ebx
        push ecx
        call query

        pop eax
        pop ebx
        add edx eax ebx
        ret edx
