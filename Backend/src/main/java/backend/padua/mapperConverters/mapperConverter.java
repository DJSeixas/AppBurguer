package backend.padua.mapperConverters;

import backend.padua.data.dto.NewClientDTO;
import backend.padua.model.Address;
import backend.padua.model.Client;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class mapperConverter {

    public static class ClientToNewDTOconverter implements Converter<Client, NewClientDTO> {

        @Override
        public NewClientDTO convert(MappingContext<Client, NewClientDTO> context) {
            Client s = context.getSource();
            NewClientDTO d = context.getDestination();
            if(d == null) {
                d = new NewClientDTO();
            }
            d.setId(s.getId());
            d.setName(s.getName());
            d.setEmail(s.getEmail());
            d.setCpf(s.getCpf());
            Address ad = s.getAddresses().stream().findFirst().orElse(null);
            if(ad != null){
                d.setAddress(ad.getAddress());
                d.setNumber(ad.getNumber());
                d.setComplement(ad.getComplement());
                d.setNeighborhood(ad.getNeighborhood());
                d.setCity(ad.getCity());
                d.setState(ad.getState());
                d.setCep(ad.getCep());
            }
            d.setTelephone(s.getTelephones().stream().findFirst().orElse(null));
            return d;
        }
    }

    public static class NewClientDTOToClientConverter implements Converter<NewClientDTO, Client> {

        @Override
        public Client convert(MappingContext<NewClientDTO, Client> context) {
            NewClientDTO s = context.getSource();
            Client d = context.getDestination();
            if (d == null) {
                d = new Client();
            }
            d.setId(s.getId());
            d.setName(s.getName());
            d.setEmail(s.getEmail());
            d.setCpf(s.getCpf());
            Address ad = Address.builder().address(s.getAddress()).number(s.getNumber()).complement(s.getComplement())
                    .neighborhood(s.getNeighborhood()).city(s.getCity()).state(s.getState()).cep(s.getCep()).build();
            d.getAddresses().add(ad);
            d.getTelephones().add(s.getTelephone());
            return d;
        }
        }
}
