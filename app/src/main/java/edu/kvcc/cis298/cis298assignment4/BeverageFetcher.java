package edu.kvcc.cis298.cis298assignment4;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BeverageFetcher {
    private static final String TAG = "Beverage Fetcher";

    /* public methods start */

    // Fetches the beverages from the web in the form of JSON data, converts it into a list of Beverages.
    public List<Beverage> fetchBeverages() {

        // List to be returned
        List<Beverage> beverages = new ArrayList<>();

        try {

            // Creates a url from the string and any additional parameters. Since there aren't any,
            //  we could just set "url" to the actual url and it wouldn't be any different.
            String url = Uri.parse("http://barnesbrothers.homeserver.com/beverageapi")
                    .buildUpon()
                    .build()
                    .toString();

            // Get the raw JSON string from the source at the URL.
            String rawJSONString = getRawStringDataFromURL(url);

            // Create a new JSONArray using the raw JSON string.
            JSONArray jsonArray = new JSONArray(rawJSONString);

            // Convert the JSONArray to the given list of beverages
            parseBeverages(beverages, jsonArray);

        } catch (IOException exception) {
            Log.e(TAG, "IOException occurred. Message: " + exception.getMessage());
        } catch (JSONException exception) {
            Log.e(TAG, "JSONException occurred. Message: " + exception.getMessage());
        }

        return beverages;
    }

    /* private methods start */

    // Essentially converts the byte data from the URL to string data.
    private String getRawStringDataFromURL(String urlString) throws IOException{
        return new String(getRawByteDataFromURL(urlString));
    }

    // Get raw bytes from a web source.
    private byte[] getRawByteDataFromURL(String urlString) throws IOException {

        // Create a URL object from the plain ol string that is a URL web address.
        // THROWS: Malformed URL Exception (seems to be covered with IOException?)
        URL url = new URL(urlString);

        // Create a new HTTP connection using the specified URL. We are now connected to the URL.
        // THROWS: IOException (if any error occurs from opening the connection)
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

        try {

            // Output stream, used to HOLD data from the connection source.
            // Specifically, holds an array of bytes.
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            // Get the input stream from the url connection. We can now actually read the data.
            // This is a stream from the connection. It's simply a tool used to read the data.
            InputStream inputStream = urlConnection.getInputStream();

            // If the response code was anything but indicating "OK", throw an IOException
            //  with information about what the server sent back (the response message) and
            //  the URL string we tried to use to connect with.
            //
            //  Maybe we sent it a typo,
            //  or the website is down, or the network entirely is down? Or would the last one trip
            //  url.openConnection()?
            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(urlConnection.getResponseMessage() +
                ": tried to connect using the URL string " + urlString);
            }

            // Keeps track of how many bytes have been read in so far.
            // Reset every time it asks inputStream how many bytes it read into its given buffer.
            int bytesRead = 0;

            // Byte buffer that will read up to 1024 bytes at a time.
            byte[] buffer = new byte[1024];

            /* READ EVERYTHING FROM THE DATA SOURCE INTO byteArrayOutputStream */
            while(true) {   // Will break whenever bytesRead is < 0

                // This line does two things
                // - First, it uses the input stream (did it already read all of the data or
                //   will it do this every time the "read" function is executed?) to read byte
                //   data into the given buffer.
                //
                // - Next, after reading byte data, it will return the amount of bytes it read.
                //   This is where we check if the byte amount was 0 (if it was, that
                //   means we've reached the end of the stream and we should break out of this loop).

                // Read the data - specifically, up to 1024 bytes of it.
                bytesRead = inputStream.read(buffer);

                // If there was no data to be read (0 bytes), stop right here - we're done.
                if (bytesRead <= 0) {
                    break;
                }

                // Write whatever was read into the INPUT buffer into the OUTPUT buffer.
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            // Close the output stream. We are done writing to it.
            byteArrayOutputStream.close();

            // Close the input stream, too? It wasn't in the in-class so I'm not sure.
            inputStream.close();

            // Return the byte array. It needs to be converted to a LITERAL byte array, as in byte[]
            return byteArrayOutputStream.toByteArray();

        } finally {

            // Disconnect from the web.
            urlConnection.disconnect();
        }
    }

    // Takes a JSONArray and makes a list of beverages out of it
    private void parseBeverages(List<Beverage> beverages, JSONArray jsonArray) throws IOException, JSONException {

        // For every element in the JSONArray...
        for (int index = 0; index < jsonArray.length(); index++) {

            // Get the JSONObject in the JSONArray located at the specified index.
            JSONObject rawJsonForBeverage = jsonArray.getJSONObject(index);

            // Create a new Beverage
            Beverage currentBeverage = new Beverage();

            /* ASSIGN ALL VALUES FROM THE JSON DATA */
            currentBeverage.setId(rawJsonForBeverage.getString("id"));
            currentBeverage.setName(rawJsonForBeverage.getString("name"));
            currentBeverage.setPack(rawJsonForBeverage.getString("pack"));
            currentBeverage.setPrice(rawJsonForBeverage.getDouble("price"));

            // If "isActive" is "1", return true. Else ("0" or anything else) return false.
            currentBeverage.setActive(rawJsonForBeverage.getString("isActive").equals("1"));

            // Finally, add the current beverage to the list of beverages we were given.
            beverages.add(currentBeverage);
        }
    }
}
