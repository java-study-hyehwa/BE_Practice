package server.domain.user.mapper;

import java.util.Arrays;
import javax.annotation.processing.Generated;
import server.domain.user.customer.Customer;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-09T09:35:56+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer userDtoToUserModel(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        byte[] customer_uuid = null;
        String customer_name = null;
        String customer_password = null;

        byte[] customer_uuid1 = customer.customer_uuid();
        if ( customer_uuid1 != null ) {
            customer_uuid = Arrays.copyOf( customer_uuid1, customer_uuid1.length );
        }
        customer_name = customer.customer_name();
        customer_password = customer.customer_password();

        Customer customer1 = new Customer( customer_uuid, customer_name, customer_password );

        return customer1;
    }
}
