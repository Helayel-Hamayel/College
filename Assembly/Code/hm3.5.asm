;write a complete program that reads an integer value and then print its factors... in FUCNTIONS

include inout.asm

.model small,c
.486
.stack 400h
.Data

  Prm1 DB 13,10,"Enter a Value here => $"
  Prm2 DB "The Value's Factors are =>$"
  Prm3 DB "*** calling the FACTOR function ***$"
  PrmE DB "***$"

.CODE

Factor proc val:word

uses ax,cx,dx

  mov cx,0
  
cont:
  inc cx
  
  mov ax,val
  cwd
  div cx
  
  or dx,dx
  jz aFactor
  jmp awDone ;are we done?
  
aFactor:  
  call putint,cx
  jmp awDone
  
awDone:  
    cmp cx,val
	je done
	jmp cont
	
done:	
    ret
    Factor endp  

.startup 

  call puts,offset PrmE
  call puts,offset Prm1
  call getint
  
  call puts,offset Prm3
  call endl
  call puts,offset Prm2
  
  
  call Factor, ax  ; FUNCTION CALLING

  call endl
  call puts,offset PrmE
  
  
  .EXIT

END
  