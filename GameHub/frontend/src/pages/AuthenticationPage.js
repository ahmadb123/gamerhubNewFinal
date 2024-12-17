{/* 
Authentication page asks users to login using their platform account - 
XBOX,PS,PC,STEAM..
1. login page - 
    1. Login is using cross-platform gamertags - 
        1. Xbox/ps/steam
        2. if login complete get user gamertag displayed on top    
2. if user doesnt have an account already registered -> 
    options with links provided -
    for example, user wants to create an account (xbox) a link will direct them to xbox 
3. if user doesnt believe in signing in. users are able to view the pages without any access to actions.
*/
}



const apiURL = "http://localhost:8080";

function togglePassword(){
    let password = document.getElementById("passwordInput");
    if(password.type === "password"){
        password.type = "text";
    }else{
        password.type = "password";
    }
}

function AuthenticationPage(){
    {/*
        FUNCTIONS WITHOUT ANY END BACK CALLS YET   
    */}
    // login authentication process -> 
    const [isAuthenticated, setAuthentication] = useState(false); // sets authentication if valid
    // username and password to pass to the backend - 
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');    

    const login = async (e) =>{
        e.preventDefault();
        if(!isAuthenticated){
            toast.error("Failed to login. Please make sure an account is already created.")
            return;
        }
        try{
            const response = await fetch(`${apiUrl}/api/auth/login`, {
                method: 'POST',
                headers: { 'Content-type': 'application/json' },
                body: JSON.stringify({ username, password }),
            }
        }
    }
}

export default AuthenticationPage;