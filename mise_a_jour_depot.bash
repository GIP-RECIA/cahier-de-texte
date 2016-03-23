#!/bin/bash

for i in $(ls uploadFile/)
do
	mkdir uploadFile/$i/Mes\ Documents
	mv uploadFile/$i/*.* uploadFile/$i/Mes\ Documents/
	
done
