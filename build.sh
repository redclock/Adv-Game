find src -name "*.java" > files.txt
javac -nowarn -d bin @files.txt

