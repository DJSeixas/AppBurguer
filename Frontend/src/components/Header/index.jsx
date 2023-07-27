import { Container } from "./styles"
import { Image } from "react-native"
import { FontAwesomeIcon } from "@fortawesome/react-native-fontawesome"
import { faUser, faBasketShopping } from "@fortawesome/free-solid-svg-icons"
import { logo } from "../../assets/images"

const Header = () => {

    return(

    <Container>
        <FontAwesomeIcon size={30} icon={ faUser } />
        <Image source ={logo}
        style={{ width: 160, height: 160 }}
        />
        <FontAwesomeIcon size={30} icon={ faBasketShopping } />
    </Container>

    )
}

export default Header