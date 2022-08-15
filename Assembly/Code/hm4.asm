;letters
;you are to write a complete assembly program to count and display number of uppercase letters using direct video buffer.

include inout.asm

.model small,c
.486
.stack 400h
.Data

strx db 50 dup(0)
prm DB 13,10,"*Insert a String here => $"
prmR DB 13,10,"*The Number of Uppercase Letters are => $"
prmR2 DB "*Displaying The Uppercase Letters of the word [BELOW] $"
prmR3 DB "=> $"
Prme DB "***$"
num dw 0

.CODE
  .startup
  ;below is to "clear" the screen just for the Video Buffer output result
  mov cx,25*80
  mov ax,0b800h
  mov es,ax
  xor di,di
  mov al,''
  mov ah,0B
  rep stosw
  
  ;RIGHT below is the location is to be printed for the Video Buffer output while the rest is the default color and the counter 
  mov di,24*160+3*2
  mov ah,00011111B
  mov bx,num
  xor bx,bx
  
  call endl
  call puts,offset Prme
  call puts,offset prm
  call gets,offset strx
  call puts,offset PrmR2
  call endl
  call puts,offset PrmR3
  lea si,strx 
  
cont: ;continue
   
  
  lodsb ;mov al,[si] /and/ add si,1
  cmp al,'$'
  JE done
  
  cmp al, 'A'
  JGE yes
  jmp ignore
  
yes: 
  cmp al, 'Z'
  JLE printit
  jmp ignore
  
  
printit : ;for UPPERCASE LETTERS
  mov ah,01001111B ;marking the UPPERCASE letters with a foreground color
  stosw
  add bx,1
  jmp cont

ignore : ;for LOWERCASE LETTERS AND OTHERS 
  mov ah,00000111B 
  stosw
  jmp cont
  
done:  
  ;printing the result of the NUMBER of uppercase letters
  call puts,offset PrmR
  call putint,bx
  call endl
  call puts,offset Prme
  .EXIT

END
  