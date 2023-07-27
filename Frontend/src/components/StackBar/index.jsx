import StackButton from "../StackButton"
import { Container, Scroll } from "./styles"


const StackBar = () => {
    return (
        <Container>
            <Scroll horizontal
            showsHorizontalScrollIndicator={false}>
            <StackButton title="Combos" />
            <StackButton title ="Artesanal"/>
            <StackButton title="Tradicional"/>
            <StackButton title="Quadrado"/>
            <StackButton title="Bebidas"/>
            <StackButton title="Porção"/>
            </Scroll>
        </Container>
    )
}

export default StackBar