#======================================== CREATING ENVIRONMENT VARIABLE ========================================

echo "Creating environment variables..."
JAVAFX_PATH="/home/davirm/Documentos/java-libs/javafx-sdk-25.0.1/lib/"
CONNECTION_PATH="/home/davirm/Documentos/java-libs/mysqlConnector/mysql-connector-j-9.7.0.jar"

#======================================== REBUILDING DIRECTORIES ========================================

echo "Rebuilding directories..."
rm -rf dist && mkdir dist &&

#======================================== COMPILING ========================================

echo "Compiling..."
find    ./src -name "*.java" > sources.txt
javac   --module-path "$JAVAFX_PATH" \
        --add-modules javafx.controls,javafx.fxml  \
        -cp "$CONNECTION_PATH" \
        -d ./dist/ @sources.txt
rm sources.txt 

#        -d ./dist/ ./src/application/*.java ./src/gui/*.java \
#        ./src/gui/util/*.java ./src/model

#======================================== ORGANIZING ./DIST/ ========================================

#echo "Creating some directories..."
#mkdir ./dist/gui/

#======================================== COPYING RESOURCES TO DIRECTORIES ========================================

echo "Copying resources..."
cp ./src/gui/*.fxml ./dist/gui/
cp ./db.properties ./dist/

#======================================== EXECUTING PROGRAM ========================================

echo "Executing..."
java    --module-path "$JAVAFX_PATH" \
        --add-modules javafx.controls,javafx.fxml \
        --enable-native-access=javafx.graphics \
        -cp "./dist:$CONNECTION_PATH" \
        application.Main