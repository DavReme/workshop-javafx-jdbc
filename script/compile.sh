echo "Creating environment variable..."
JAVAFX_PATH="/home/davirm/Documentos/java-libs/javafx-sdk-25.0.1/lib/"
echo "Rebuilding directories..."
rm -rf dist && mkdir dist &&
echo "Compiling..."
javac   --module-path "$JAVAFX_PATH" \
        --add-modules javafx.controls,javafx.fxml  \
        -d ./dist/ ./src/application/*.java
echo "Creating some directories..."
mkdir ./dist/gui/
echo "Copying resources..."
cp ./src/gui/*.fxml ./dist/gui/
echo "Executing..."
java    --module-path "$JAVAFX_PATH" \
        --add-modules javafx.controls,javafx.fxml \
        --add-opens gui/gui=MainView.fxml \
        -cp ./dist application.Main