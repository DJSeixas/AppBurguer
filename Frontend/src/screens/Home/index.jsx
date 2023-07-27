import { StatusBar } from "expo-status-bar"
import Header from "../../components/Header"
import { Container } from "./styles"
import StackBar from "../../components/StackBar"
import { useFonts } from 'expo-font';
import { useCallback } from 'react';


import * as SplashScreen from 'expo-splash-screen';


SplashScreen.preventAutoHideAsync();


const Home = () => {

    const [fontsLoaded] = useFonts({
        'tek-mid': require('../../assets/fonts/Tektur-Medium.ttf'),
        'tek-bold': require('../../assets/fonts/Tektur-Bold.ttf'),
        'tek-xbold': require('../../assets/fonts/Tektur-ExtraBold.ttf')
      });
    
      const handleOnLayout = useCallback(async () => {
        if(fontsLoaded) {
          await SplashScreen.hideAsync(); 
        }
      }, [fontsLoaded]);
    
      if(!fontsLoaded){
        return null; 
      }

    return (
        <Container onLayout={handleOnLayout}> 
            <StatusBar style="auto" />
            <Header />
            <StackBar />
        </Container>
    )
}

export default Home