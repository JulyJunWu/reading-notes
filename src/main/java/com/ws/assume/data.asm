assume cs:code,ds:data,ss:stack
; 将数据/代码/栈放入不同段
data segment
    dw 0123H,0456H,0789H,0abcH
data ends
stack segment
    dw 0,0,0,0,0,0,0,0,0,0,0,0,0,0
stack ends
code segment
start:
    ; 初始化各段寄存器
    ; 入栈
    ; 出栈
    mov ax,4c00H
    int 21h
code ends
end start
