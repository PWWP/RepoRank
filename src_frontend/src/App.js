import React, { useState, useEffect } from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Cookies from 'universal-cookie';
import Header from './components/Header';
import Footer from './components/Footer';
import logo from './logo.svg';
import './App.css';
import Topbar from "./components/Topbar";
import Addrepo from "./pages/Addrepo";
import Homepage from "./pages/Homepage";
import Userinfo from "./pages/Userinfo";
import Logout from "./pages/Logout";
import Toplist from "./pages/Toplist";
import Login from "./pages/Login";
import RepositoryDetails from "./pages/RepositoryDetails";
import 'bootstrap/dist/css/bootstrap.min.css';
import Register from "./pages/Register";

const cookies = new Cookies();

function App() {
    const [token, setToken] = useState('');

    const handleLogin = (newToken) => {
        setToken(newToken);
        cookies.set('token', newToken, { path: '/' });
        console.log('Zalogowano pomyślnie. Token:', newToken);
    };

    const handleLogout = () => {
        setToken('');
        cookies.remove('token', { path: '/' });
        console.log('Wylogowano pomyślnie.');
        // Dodaj logikę przekierowania użytkownika na stronę logowania lub innej po wylogowaniu
    };

    useEffect(() => {
        // Sprawdź, czy istnieje cookie z tokenem
        const storedToken = cookies.get('token');
        if (storedToken) {
            // Jeśli token istnieje, zapisz go w stanie aplikacji
            setToken(storedToken);
            console.log('Znaleziono token w cookies:', storedToken);
        } else {
            console.log('Nie znaleziono tokenu w cookies.');
        }
    }, []);

    return (
        <div className="App">
            <Topbar token={token} onLogout={handleLogout} />
            <main>
                <BrowserRouter>
                    <Routes>
                        <Route path="/addrepo" element={<Addrepo token={token} />} />
                        <Route path="/toplist" element={<Toplist token={token} />} />
                        <Route path="/userinfo" element={<Userinfo token={token} />} />
                        <Route path="/logout" element={<Logout onLogout={handleLogout} />} />
                        <Route path="/login" element={<Login onLogin={handleLogin} token={token}/>} />
                        <Route path="/register" element={<Register token={token}/>} />
                        <Route path="/repo/:repositoryId" element={<RepositoryDetails token={token} />} />
                        <Route path="/" element={<Homepage token={token} />} />
                    </Routes>
                </BrowserRouter>
            </main>
            <Footer />
        </div>
    );
}

export default App;
