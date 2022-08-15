;Write a complete assembly program that will read a string and then
;computes number of 'a' within the string.

include inout.asm

.model small,c
.486
.stack 400h
.Data
  Prm1 DB 13,10,"Enter a String here => $"
  Prm2 DB "The Number of a's in the String is =>$"
  Prm3 DB "You entered an empty String$"
  Prm4 DB "***$"
  wrd DW 10 DUP('$')
  emp DW 0 ;FOR TELLING THE USER IF HE ENTERED AN EMPTY STRING (emp => is it empty?)
.CODE
  .startup
  
  call endl
 
    call puts,offset Prm4
    call puts,offset Prm1
	call gets,offset wrd
	mov cx,emp
	xor bx,bx
	lea si,wrd
	
continue:	
	mov al,[si]
	cmp al,'$'
	je finished
	
	cmp al,'a'
	JE countem
	cmp al,'A'
	JE countem ;[IF-OR STATEMENT] IF THE LETTER IS EITHER 'a' OR 'A' 
	
	inc si
	inc cx
	jmp continue
	
countem:	
	inc bx
	inc si
	inc cx
	jmp continue
	
	
  
finished:  
    call puts,offset Prm2
	call putint,bx
	call endl
	cmp cx,0
	JNE DONE
    call puts,offset Prm3
	call endl
    DONE:
	call puts,offset Prm4
    call endl
  
  .EXIT

END
  