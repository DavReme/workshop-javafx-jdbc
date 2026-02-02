echo "Creating environment variable..."
JAVAFX_PATH="/home/davirm/Documentos/java-libs/javafx-sdk-25.0.1/lib/"
echo "Rebuilding directories..."
rm -rf dist && mkdir dist &&
echo "Compiling..."
javac --module-path "$JAVAFX_PATH" --add-modules javafx.controls -d ./dist/ ./src/application/*.java
echo "Executing..."
java --module-path "$JAVAFX_PATH" --add-modules javafx.controls -cp ./dist application.Main