;Write a complete Assembly program that rotates a STRING continuously in the middle of the
;screen  using direct video buffer..

include inout.asm

.model small,c
.486
.stack 400h
.Data

strx Db "Hello$"
Prme DB "***$"
x dw 0

.CODE

CLS proc 
uses cx,ax,es,di

  
  mov ax,0b800h
  mov es,ax
  
  mov cx,80*1
  mov di,26*80
  mov al,' '
  mov ah,0B
  rep stosw


ret 
cls endp


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

DELAY proc 
uses ecx

  mov ecx,10000
inf:
  nop
  nop
  nop
 
  loop inf  
  
ret 
DELAY endp

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  .startup
  
  mov cx,25*80
  mov ax,0b800h
  mov es,ax
  xor di,di
  mov al,' '
  mov ah,0B
  rep stosw 
 
 
  mov cx,25*80
  mov ax,0b800h
  
  mov di,26*80
  
  call puts,offset Prme
  
  lea si,strx 
  mov ah ,0111b
  xor bx,bx
  
  
  mov cx,100
  jmp yea
  
    
GOBACK:


call DELAY
call DELAY
call DELAY
call DELAY
call DELAY
call DELAY

call CLS
cmp di,2238
jge reset 
jmp dos

reset:
xor bx,bx
mov di,26*80
add bx,1
add di,bx
;jmp ig

dos:
mov di,26*80
add bx,1
add di,bx
ig:

lea si,strx 

yea:
cont: 
   
  mov al,[si]
  cmp al,'$'
  JE done
   
  stosw
  add si,1
  
  add bx,1
  jmp cont

done:  

loop GOBACK

  
  .EXIT

END
  