;You are to write a complete program that reads an array of 10 integers then display
;the number of even and odd numbers stored in the above array.


include inout.asm

.model small,c
.486
.stack 400h
.Data
  Prm1 DB 13,10,"Enter a Integer into ARRAY here => $"

  Prm2 DB "The Number of EVEN in the Array is =>$"
  Prm3 DB "The Number of ODD in the Array is =>$"

  Prme DB "***$"
  num  DW 10 DUP(0)
  siz DW 10

  EVN DW 0
  ODD DW 0

.CODE
  .startup

  call endl

    call puts,offset Prme
	mov DI, offset num
	mov cx,siz

 next:
    call puts,offset Prm1
	call getint
	mov [DI],AX
	add DI,2

	loop next




	;mov DX,0 ;EVEN Counter
	;mov BX,0 ;ODD Counter
	mov si, offset num
	mov cx,siz
;
Cont:
	mov AX,[si]

	add si,2
	
	SHR AX,1
	JC IsODD
	
	inc EVN
	JMP Ignore

 IsODD:
    inc ODD

Ignore:
	LOOP Cont





	call endl

	call puts,offset Prm2
	call putint,EVN

	call endl

	call puts,offset Prm3
	call putint,ODD

	call endl

	call puts,offset Prme
    call endl

  .EXIT

END
