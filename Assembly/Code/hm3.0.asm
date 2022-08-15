;write a complete program that reads an integer value and then print its factors...

include inout.asm

.model small,c
.486
.stack 400h
.Data

  Prm1 DB 13,10,"Enter a Value here => $"
  Prm2 DB "The Value's Factors are =>$"
  PrmE DB "***$"
  
  val DW 0
  
.CODE
  .startup
  call puts,offset PrmE
  call puts,offset Prm1
  call getint
  call puts,offset Prm2
  
  
  mov val,ax
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
  call endl
  call puts,offset PrmE
  
  .EXIT

END
  