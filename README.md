# JsonToCsvConverter
The program query the API with the user input and create a CSV file from it. The CSV file have the form: _id, name, type, latitude, longitude

API endpoint:
http://api.goeuro.com/api/v2/position/suggest/en/CITY_NAME

Where CITY_NAME is the string that the user has entered as a parameter when calling the tool, e.g.
http://api.goeuro.com/api/v2/position/suggest/en/Berlin

This project has been created using IntelliJ IDEA. You can check source code in "src" folder.
If you need a "quick start" you can find .jar file in "out" folder (out\artifacts\JsonToCsvConverter_jar\JsonToCsvConverter.jar)
You can run it using the following command(after that you'll get "output.csv" file):
java -jar JsonToCsvConverter.jar Berlin
