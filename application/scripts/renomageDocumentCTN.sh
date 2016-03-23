#!/bin/bash
nom='/Documents du CTN'
find . -name "Mes Documents*" | while read i 
do
	mv "$i/" "$(dirname "$i")""$nom" 
done;
