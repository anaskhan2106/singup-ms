package com.mapper;

import com.domain.DocumentStore;
import com.domain.DriverProfile;
import com.dto.DocumentStoreDto;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.annotation.processing.Generated;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-02T18:20:44+0530",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.19 (Azul Systems, Inc.)"
)
@Component
public class MapperClassImpl extends MapperClass {

    private final DatatypeFactory datatypeFactory;

    public MapperClassImpl() {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        }
        catch ( DatatypeConfigurationException ex ) {
            throw new RuntimeException( ex );
        }
    }

    @Override
    public DocumentStore documentStoreDtoToDocumentStore(DocumentStoreDto documentStoreDto) {
        if ( documentStoreDto == null ) {
            return null;
        }

        DocumentStore documentStore = new DocumentStore();

        documentStore.setDriverProfile( documentStoreDtoToDriverProfile( documentStoreDto ) );
        documentStore.setId( documentStoreDto.getId() );
        documentStore.setDocumentName( documentStoreDto.getDocumentName() );
        documentStore.setDocumentType( documentStoreDto.getDocumentType() );
        documentStore.setUploadedBy( documentStoreDto.getUploadedBy() );
        documentStore.setUploadedTs( xmlGregorianCalendarToCalendar( dateToXmlGregorianCalendar( documentStoreDto.getUploadedTs() ) ) );
        documentStore.setComments( documentStoreDto.getComments() );

        return documentStore;
    }

    private Calendar xmlGregorianCalendarToCalendar( XMLGregorianCalendar xcal ) {
        if ( xcal == null ) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( xcal.toGregorianCalendar().getTimeInMillis() );
        return cal;
    }

    private XMLGregorianCalendar dateToXmlGregorianCalendar( Date date ) {
        if ( date == null ) {
            return null;
        }

        GregorianCalendar c = new GregorianCalendar();
        c.setTime( date );
        return datatypeFactory.newXMLGregorianCalendar( c );
    }

    protected DriverProfile documentStoreDtoToDriverProfile(DocumentStoreDto documentStoreDto) {
        if ( documentStoreDto == null ) {
            return null;
        }

        DriverProfile driverProfile = new DriverProfile();

        driverProfile.setUserId( documentStoreDto.getUser_id() );

        return driverProfile;
    }
}
