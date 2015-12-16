package edu.kvcc.cis298.cis298assignment4;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BeverageFragment extends Fragment {

    // Request for a contact request code
    private static final int REQUEST_CONTACT = 0;

    private static final String TAG = "BeverageFragment";

    private static final String ARG_BEVERAGE_ID = "crime_id";   // key-value key used in the "constructor" to identify the ID value.

    private EditText mId;                                       // Widgets for this fragment.
    private EditText mName;
    private EditText mPack;
    private EditText mPrice;
    private CheckBox mActive;

    // Added in Assignment 4
    private Button mSelectContactButton;
    private Button mSendBeverageDetailsButton;

    // Gathered contact name and email
    private String mContactName = null;
    private String mContactEmail = null;

    private Beverage mBeverage;                                 // Beverage that will be shown with this fragment.

    // Essentially acts as a constructor for this fragment.
    public static BeverageFragment newInstance(String id) {
        Bundle args = new Bundle();                             // Bundle for holding arguments.
        args.putString(ARG_BEVERAGE_ID, id);                    // Put the "ID" value in the bundle.
        BeverageFragment fragment = new BeverageFragment();     // Create a new fragment.
        fragment.setArguments(args);                            // Shove that bundle in the fragment.
        return fragment;                                        // Return the fragment.
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String beverageId = getArguments().getString(ARG_BEVERAGE_ID);              // Retrieve beverage ID from the bundle that was stuffed into this fragment in the "constructor".
        mBeverage = BeverageCollection.get(getActivity()).getBeverage(beverageId);  // Get the appropriate beverage by using its ID.
    }

    @Override
    public void onPause() {
        super.onPause();

        // Beverages get modified in BeverageFragment, so when this fragment pauses, write out the changes and update the database.
        BeverageCollection.get(getActivity()).updateBeverage(mBeverage);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Retrieve inflated view using the provided inflater.
        View view = inflater.inflate(R.layout.fragment_beverage, container, false);

        // Get handles to the widget controls in the view
        mId = (EditText) view.findViewById(R.id.beverage_id);
        mName = (EditText) view.findViewById(R.id.beverage_name);
        mPack = (EditText) view.findViewById(R.id.beverage_pack);
        mPrice = (EditText) view.findViewById(R.id.beverage_price);
        mActive = (CheckBox) view.findViewById(R.id.beverage_active);

        // added in assignment 5
        mSelectContactButton = (Button) view.findViewById(R.id.select_contact_button);
        mSendBeverageDetailsButton = (Button) view.findViewById(R.id.send_beverage_details_button);

        //Set the widgets to the properties of the beverage
        mId.setText(mBeverage.getId());
        mId.setEnabled(false);
        mName.setText(mBeverage.getName());
        mPack.setText(mBeverage.getPack());
        mPrice.setText(Double.toString(mBeverage.getPrice()));
        mActive.setChecked(mBeverage.isActive());

        // added in assignment 4

        // If the user has a default contacts app, enable this button and disable the other one.
        // Else, vise-versa.
        // I probably could have just used the boolean value this method returns as the "true" or "false"
        //    but that would look weird.
        if (userHasDefaultContactsApp()) {
            mSelectContactButton.setEnabled(true);          // Enable for now. Will be disabled after contact is selected.
            mSendBeverageDetailsButton.setEnabled(false);   // Disable for now. Will be enabled after contact is selected.
        } else {
            mSelectContactButton.setEnabled(false);         // Disable indefinitely.
            mSendBeverageDetailsButton.setEnabled(true);    // Enable indefinitely.
        }

        //Text changed listener for the id. It will not be used since the id will be always be disabled.
        //It can be used later if we want to be able to edit the id.
        mId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBeverage.setId(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //Text listener for the name. Updates the model as the name is changed
        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBeverage.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //Text listener for the Pack. Updates the model as the text is changed
        mPack.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBeverage.setPack(s.toString());
                BeverageCollection.get(getActivity()).updateBeverage(mBeverage);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //Text listener for the price. Updates the model as the text is typed.
        mPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //If the count of characters is greater than 0, we will update the model with the
                //parsed number that is input.
                if (count > 0) {
                    mBeverage.setPrice(Double.parseDouble(s.toString()));
                    BeverageCollection.get(getActivity()).updateBeverage(mBeverage);
                //else there is no text in the box and therefore can't be parsed. Just set the price to zero.
                } else {
                    mBeverage.setPrice(0);
                    BeverageCollection.get(getActivity()).updateBeverage(mBeverage);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //Set a checked changed listener on the checkbox
        mActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBeverage.setActive(isChecked);
            }
        });

        // Listener for Select Contact button. Is disabled if no default contacts app.
        mSelectContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Intent to pick a contact.
                final Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

                // Send out the implicit intent. Results should come back in onActivityResult.
                startActivityForResult(pickContactIntent, REQUEST_CONTACT);
            }
        });

        mSendBeverageDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Intent to email.
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                // The type of message we are sending is an email.
                emailIntent.setType("message/rfc822");

                // Set subject line.
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Beverage App Item Information");

                // Set email body. mContactName may turn out to be null; that is fine.
                emailIntent.putExtra(Intent.EXTRA_TEXT, getBeverageEmailBody(mContactName));

                // If we have an email to work with...
                if (mContactEmail != null) {

                    // Put the email in the intent.
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { mContactEmail });
                }

                // Finally, fulfill the request.
                startActivity(Intent.createChooser(emailIntent, "Send email using..."));
            }
        });

        //Lastly return the view with all of this stuff attached and set on it.
        return view;
    }

    // Returns information about the beverage this fragment is working with.

    // receiverName is allowed to be null, in case they can not supply one.
    @Nullable
    private String getBeverageEmailBody(String receiverName) {

        // String receiverName
        String promptString = getString(R.string.email_body_prompt);        // Set prompt of email to set string in resources file.

        String idString = mBeverage.getId();
        String nameString = mBeverage.getName();
        String packString = mBeverage.getPack();

        // Decimal formatter to 2 decimal places, not dropping trailing zeroes
        DecimalFormat df = new DecimalFormat("#.00");
        String priceString = df.format(mBeverage.getPrice());

        String isActiveString;

        // Set isActiveString depending on the boolean value of isActive()
        if (mBeverage.isActive()) {
            isActiveString = "Currently Active";
        } else {
            isActiveString = "Current Inactive";
        }

        /* START BUILDING BODY OF EMAIL */
        String bodyOfEmail = new String();

        // Is there a receiver name? Is it also not empty?
        if (receiverName != null && ! receiverName.trim().equals("")) {

            // Add name to body of email.
            bodyOfEmail += receiverName + "," + "\n" + "\n";
        }

        // Add rest of email body.
        bodyOfEmail += promptString + "\n" + "\n" +
                "ID: " + idString + "\n" +
                "Name: " + nameString + "\n" +
                "Pack: " + packString + "\n" +
                "Price: " + priceString + "\n" +
                isActiveString;

        // Return the email body string.
        return bodyOfEmail;
    }

    private boolean userHasDefaultContactsApp() {
        // Create an intent to pick a contact. We are just using it to ask the package manager
        //   if the user has a default Contacts app.
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        // Get reference to package manager.
        PackageManager packageManager = getActivity().getPackageManager();

        // If the package manager reports that the user does not have a default contacts app...
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            return false;       // They don't have one.
        } else {
            return true;        // They have one.
        }
    }

    // What happens after the "Select Contact" button is pressed and a contact is supposedly selected.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // If this is the fulfillment of the "Get a contact for me" request...
        if (requestCode == REQUEST_CONTACT) {

            // If there was actual data returned
            if (data != null) {

                // Get name and email using the contact URI.
                String[] nameAndEmail = getNameAndEmailFromContact(data.getData());

                // Assign to class-level variables.
                mContactName = nameAndEmail[0];
                mContactEmail = nameAndEmail[1];

                // Extract data. name or email will be null if the contact wasn't selected or it didn't have an associated email.
                if (mContactName == null) {

                    // Contact not valid.
                    Toast.makeText(getContext(), R.string.contact_not_selected, Toast.LENGTH_SHORT).show();
                } else {

                    // Contact was valid. Disable button and notify user.

                    // Let user know what contact they selected.
                    String contactMessage = "Contact " + "\"" + mContactName + "\" selected with email " + mContactEmail;
                    Toast.makeText(getContext(), contactMessage, Toast.LENGTH_SHORT).show();

                    // Disable contact button - they've now selected a contact.
                    mSelectContactButton.setEnabled(false);

                    // Enable "Send Beverage Details" button. It will use the class-level variables we
                    //    just defined to create the data.
                    mSendBeverageDetailsButton.setEnabled(true);

                }
            }
            else {
                // No data returned.
                Toast.makeText(getContext(), R.string.contact_not_selected, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // [0] is name, [1] is email.
    public String[] getNameAndEmailFromContact(Uri contactUri) {

        // List of names.
        ArrayList<String> names = new ArrayList<String>();

        // Content resolver.
        ContentResolver cr = getActivity().getContentResolver();

        // Cursor to query contact database
        Cursor cur = cr.query(contactUri, null, null, null, null);

        // Variables to hold name and email
        String name = null;
        String email = null;

        // If there were results...
        if (cur.getCount() > 0) {

            // While the cursor can move to another row
            while (cur.moveToNext()) {

                // Get the id of the contact.
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

                // Create another cursor, this time querying for the email.
                Cursor cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);

                // While the cursor can move to another row
                while (cur1.moveToNext()) {

                    // Get the name of the contact.
                    name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                    // Get the email of the contact.
                    email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    if(email!=null){
                        names.add(name);
                    }
                }
                cur1.close();
            }
        }

        // Return results.
        return new String[] {name, email};
    }



}
