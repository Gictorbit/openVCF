import json
import xlsxwriter

def main():
    with open('/mnt/MyFile/PROJECT/openVCF/files/contacts.json','r') as file:
        contacts = json.loads(file.read())
    
    # print(json.dumps(contacts,indent=4, ensure_ascii=False))

    contactExcel = xlsxwriter.Workbook('contacts.xlsx')
    contactSheet = contactExcel.add_worksheet()
    row =0
    for vcard in contacts:
        col=0
        vcard=list(vcard.values())[0]
        if vcard['firstname']==None :
            vcard['firstname']=''

        if vcard['lastname']==None:
            vcard['lastname']=''

        # print(vcard)
        contactSheet.write(row,col,vcard['firstname'])
        contactSheet.write(row,col+1,vcard['lastname'])
        contactSheet.write(row,col+2,vcard['fullname'])

        numbers=list(vcard['phoneNumbers'].values())
        numcol=3
        for num in numbers:
            contactSheet.write(row,numcol,num)
            numcol+=1
        
        
        notes=list(vcard['notes'].values())
        notcol = numcol+1
        for note in notes:
            contactSheet.write(row,notcol,note)
            notcol+=1


        row+=1

    contactExcel.close()

if __name__ == '__main__':
    main()
