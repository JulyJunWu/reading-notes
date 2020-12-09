assume cs:code,ds:dataMsg
; 大写转小写
dataMsg segment
  db 'BasIC'
datasg ends
code segment
start:
    mov ax,dataMsg
    mov ds,ax
    mov bx,0
    mov cx,5
 s: mov al [bx]
    and al 1101111b
    mov [bx],al
    inc bx
    loop s

    mov ax,4c00h
    int 21h
code ends
end start
