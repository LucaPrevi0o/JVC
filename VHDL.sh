if test $# -ne 1
then
    echo "Usage: $0 <filename>"
    exit
fi

rm -r java/*
javac -d java VHDL.java
reset
java -cp java VHDL $1