
// SEGMENT TREE
// refreshed version: with ldea & stea
// yeah its good
// init: read n
// add: 1 l r x
// query: 2 l r
// end: others


ldc esp 4096
ldc ebp 4096

ldc ebx 16384
push ebx


enter
ldc eax 1
input ebx
push eax
push ebx

call build

# loop

input edx
ldc ecx 1
equ ebx ecx edx
jnz l1 ebx
ldc ecx 2
equ ebx ecx edx
jnz l2 ebx

halt
# l1
ldstk eax 4
//output eax
enter
push eax
input edx
push edx
input edx
push edx
input edx
push edx
call add
jmp loop
# l2
ldstk eax 4
//output eax
// halt
enter
push eax
input edx
push edx
input edx
push edx
call query
pop edx
output edx
jmp loop


# build
    enter
    call new24
    pop eax
    ldstk ebx 0
    ldstk ecx 4
    stea eax 0 ebx
    stea eax 4 ecx
    ldc edx 0
    stea eax 16 edx
    stea eax 20 edx
    lss edx ebx ecx
    jnz buildlr edx
    ldc edx 0
    stea eax 8 edx
    stea eax 12 edx
    ret eax
    # buildlr
        push eax

        ldstk ebx 0
        ldstk ecx 4
        add edx ebx ecx
        ldc eax 2
        div edx edx eax
        enter
        push ebx
        push edx
        call build
        pop edx
        ldstk eax 8
        stea eax 8 edx

        ldstk ebx 0
        ldstk ecx 4
        add edx ebx ecx
        ldc eax 2
        div edx edx eax
        ldc eax 1
        add edx edx eax
        enter
        push edx
        push ecx
        call build
        pop edx
        ldstk eax 8
        stea eax 12 edx

        ret eax

# add
    ldstk eax 0

    ldstk ebx 4         // left
    ldea ecx eax 4      // rbound
    gtr edx ebx ecx
    jz add1 edx
    leave
    # add1
    ldstk ebx 8         // right
    ldea ecx eax 0      // lbound
    lss edx ebx ecx
    jz add2 edx
    leave
    # add2
    ldstk ebx 4         // left
    ldea ecx eax 0      // lbound
    leq edx ebx ecx
    jz adds edx
    ldstk ebx 8         // right
    ldea ecx eax 4      // rbound
    geq edx ebx ecx
    jz adds edx
    ldstk ebx 12        // x
    enter
    push eax
    push ebx
    call addseg
    leave
    # adds
    ldstk eax 0         // ptr
    enter
    push eax
    call pushdown

    ldstk eax 0         // ptr
    ldea eax eax 8
    ldstk ebx 4
    ldstk ecx 8
    ldstk edx 12
    enter
    push eax
    push ebx
    push ecx
    push edx
    call add

    ldstk eax 0         // ptr
    ldea eax eax 12
    ldstk ebx 4
    ldstk ecx 8
    ldstk edx 12
    enter
    push eax
    push ebx
    push ecx
    push edx
    call add

    ldstk eax 0         // ptr
    enter
    push eax
    call update
    leave


# addseg
    ldstk eax 0     // ptr
    ldstk ebx 4     // x
    ldea ecx eax 0  // lbound
    ldea edx eax 4  // rbound
    sub edx edx ecx // rbound-lbound
    ldc ecx 1
    add edx edx ecx // rbound-lbound+1
    mul edx edx ebx // (rbound-lbound+1)*x
    ldea ecx eax 16 // val
    add edx edx ecx // (rbound-lbound+1)*x+val
    stea eax 16 edx
    ldea ecx eax 20 // lazy
    add edx ecx ebx // lazy+x
    stea eax 20 edx
    leave
# pushdown
    ldstk eax 0     // ptr
    ldea ecx eax 20 // lazy
    ldc edx 0
    stea eax 20 edx // lazy = 0

    push ecx
    ldstk eax 0     // ptr
    ldea ebx eax 8  // l
    enter
    push ebx
    push ecx
    call addseg

    pop ecx
    ldstk eax 0     // ptr
    ldea ebx eax 12 // r
    enter
    push ebx
    push ecx
    call addseg

    leave

# update
    ldstk eax 0
    ldea ebx eax 8
    ldea edx ebx 16
    ldea ebx eax 12
    ldea ecx ebx 16
    add edx edx ecx
    stea eax 16 edx
    // output eax
    // output edx
    // halt
    leave
# new24
    mov ebx 4096
    ldc ecx 24
    add ecx ebx ecx
    movr 4096 ecx
    ret ebx


# query
	ldstk eax 0
	ldstk ebx 4     // left
	ldea ecx eax 4  // rbound
	gtr edx ebx ecx
	jz query1 edx
	xor edx edx edx
	ret edx
	# query1
	ldstk ebx 8     // right
	ldea ecx eax 0  // lbound
	lss edx ebx ecx
	jz query2 edx
	xor edx edx edx
	ret edx
	# query2
	ldstk ebx 4     // left
	ldea ecx eax 0  //lbound
	leq edx ebx ecx
	jz querys edx
	ldstk ebx 8     // right
	ldea ecx eax 4  // rbound
	geq edx ebx ecx
	jz querys edx
	ldea edx eax 16
	ret edx
	# querys
	ldstk eax 0
	enter
	push eax
	call pushdown

	ldstk eax 0
	ldea eax eax 8
	ldstk ebx 4
	ldstk ecx 8
	enter
	push eax
	push ebx
	push ecx
	call query

	ldstk eax 0
	ldea eax eax 12
	ldstk ebx 4
	ldstk ecx 8
	enter
	push eax
	push ebx
	push ecx
	call query
	pop ecx
	pop edx
	// halt
	add edx edx ecx

	ret edx
