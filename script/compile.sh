#======================================== CREATING ENVIRONMENT VARIABLE ========================================

echo "Creating environment variable..."
JAVAFX_PATH="/home/davirm/Documentos/java-libs/javafx-sdk-25.0.1/lib/"

#======================================== REBUILDING DIRECTORIES ========================================

echo "Rebuilding directories..."
rm -rf dist && mkdir dist &&

#======================================== COMPILING ========================================

echo "Compiling..."
javac   --module-path "$JAVAFX_PATH" \
        --add-modules javafx.controls,javafx.fxml  \
        -d ./dist/ ./src/**/**/*.java ./src/**/*.java 

#        -d ./dist/ ./src/application/*.java ./src/gui/*.java \
#        ./src/gui/util/*.java ./src/model

#======================================== ORGANIZING ./DIST/ ========================================

#echo "Creating some directories..."
#mkdir ./dist/gui/

#======================================== COPYING RESOURCES TO DIRECTORIES ========================================

echo "Copying resources..."
cp ./src/gui/*.fxml ./dist/gui/

#======================================== EXECUTING PROGRAM ========================================

echo "Executing..."
java    --module-path "$JAVAFX_PATH" \
        --add-modules javafx.controls,javafx.fxml \
        --add-opens gui/gui=MainView.fxml \
        -cp ./dist application.Main