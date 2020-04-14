package vcfToJson;

import java.io.File;
import java.io.IOException;
import java.util.*;
import ezvcard.*;
import ezvcard.Ezvcard;
import ezvcard.io.json.JCardWriter;
import ezvcard.property.Address;
import ezvcard.property.Email;
import ezvcard.property.Note;
// import ezvcard.io.json.JCardWriter;
// import ezvcard.io.text.VCardReader;
import ezvcard.property.Telephone;
import java.io.FileWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class App {
    public static void main(final String []args){
        String vcfPath = "/mnt/MyFile/PROJECT/openVCF/files/contacts.vcf";
        String jsonPath = "/mnt/MyFile/PROJECT/openVCF/files/contacts.json";
        try{
            getSummaryJson(vcfPath,jsonPath);
        }catch(Exception e){
            System.out.println("error");
        }
        
    }

    public static void getTotalJson(String vcfPath,String jsonPath)throws Exception{
        
        File file = new File(vcfPath);
        List<VCard> vcards = Ezvcard.parse(file).all();

        File jsonfile = new File(jsonPath);
        JCardWriter writer = new JCardWriter(jsonfile);
		writer.setPrettyPrint(true);
        try {
            for (VCard vcard : vcards) {
                writer.write(vcard);
            }
        } finally {
            writer.close();
        }
    }

    public static void getSummaryJson(String vcfPath,String jsonPath)throws Exception{
        File file = new File(vcfPath);
        List<VCard> vcards = Ezvcard.parse(file).all();
        
        VCard vcard;
        JSONArray contactList = new JSONArray();
        try {
            for(int i=0;i<vcards.size();i++){
                vcard=vcards.get(i);
                JSONObject contactDetails = new JSONObject();
                JSONObject contactObject = new JSONObject(); 

                String firstname=vcard.getStructuredName().getGiven();
                String lastname=vcard.getStructuredName().getFamily();
                String fullname=vcard.getFormattedName().getValue();

                contactDetails.put("firstname", firstname);
                contactDetails.put("lastname", lastname);
                contactDetails.put("fullname", fullname);

                List <Telephone> phoneNumbers = vcard.getTelephoneNumbers();
                List <Email> emails = vcard.getEmails();
                List<Address> addresses = vcard.getAddresses();
                List<Note> notes =vcard.getNotes();

                JSONObject phoneNumberJson = new JSONObject();
                JSONObject emailAddressJson = new JSONObject();
                JSONObject AddressesJson = new JSONObject();
                JSONObject notesJson = new JSONObject();


                for(Note note : notes){
                    notesJson.put(note.getType(), note.getValue());
                }
                contactDetails.put("notes",notesJson);
                
                for (Address address : addresses){
                    AddressesJson.put(address.getTypes(), address.getExtendedAddressFull());
                    // System.out.println(address.getTypes()+"\t"+address.getExtendedAddressFull());
                }
                contactDetails.put("addresses", AddressesJson);

                for (Email email : emails){
                    emailAddressJson.put(email.getTypes(),email.getValue());
                }
                contactDetails.put("emails",emailAddressJson);
                
                
                for(int number=0;number<phoneNumbers.size();number++){
                    Telephone num = phoneNumbers.get(number);
                    phoneNumberJson.put(num.getTypes(),num.getText());
                }
                contactDetails.put("phoneNumbers",phoneNumberJson);
                
                contactObject.put(fullname, contactDetails);
                contactList.add(contactObject);

            }
        }catch(NullPointerException err){
            System.out.println("Error");
        }

        try (FileWriter jsonfile = new FileWriter(jsonPath)) {
 
            jsonfile.write(contactList.toJSONString());
            jsonfile.flush();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
