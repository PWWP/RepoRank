import React, { useState, useEffect } from 'react';

const Login = ({ onLogin, token }) => {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
    });

    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    useEffect(() => {
        if (token) {
            setSuccess('Jesteś już zalogowany');
            setTimeout(() => {
                window.location.href = '/';
            }, 2000);
        }
    }, [token]);

    const ErrorMessage = ({ message }) => {
        return (
            <div className="alert alert-danger mt-2 mb-2" role="alert">
                {message}
            </div>
        );
    };

    const SuccessMessage = ({ message }) => {
        return (
            <div className="alert alert-success mt-2 mb-2" role="alert">
                {message}
            </div>
        );
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8085/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });

            const data = await response.json();

            if (response.status === 200) {
                setSuccess('Zalogowano pomyślnie');
                onLogin(data.data.token);
                setTimeout(() => {
                    window.location.href = '/';
                }, 2000);
            } else if (response.status === 400 && data.message) {
                const errorFields = Object.keys(data.message);
                const errorMessage = errorFields.map((field) => `${field}: ${data.message[field]}`).join(', ');
                setError(`Błędne dane uwierzytelniające: ${errorMessage}`);
                setSuccess('');
            } else {
                const errorData = await response.json();
                setError(errorData.message);
                setSuccess('');
            }
        } catch (error) {
            console.error('Error during login:', error);
            setError('Wystąpił błąd podczas logowania');
            setSuccess('');
        }
    };

    // Jeżeli token istnieje, wyświetl komunikat o zalogowaniu
    if (token) {
        return (
            <div className="content-container">
                <div className="container">
                    <div className="row">
                        <div className="column-lg-5 column-md-12">
                            <div className="row">
                                <div className="card">
                                    <div className="card-header">Logowanie</div>
                                    <div className="card-body">
                                        {success && <SuccessMessage message={success} />}
                                        <p className="register-link">Zostaniesz przekierowany na stronę główną za 2 sekundy...</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    // Jeżeli token nie istnieje, renderuj standardowy formularz logowania
    return (
        <div className="content-container">
            <div className="container">
                <div className="row">
                    <div className="column-lg-5 column-md-12">
                        <div className="row">
                            <div className="card">
                                <div className="card-header">Logowanie</div>
                                <div className="card-body">
                                    {error && <ErrorMessage message={error} />}
                                    {success && <SuccessMessage message={success} />}
                                    <form method="POST" onSubmit={handleSubmit}>
                                        <div className="form-group">
                                            <input
                                                type="text"
                                                className="form-input"
                                                name="username"
                                                placeholder="E-Mail/Login"
                                                onChange={(e) => setFormData({ ...formData, username: e.target.value })}
                                            />
                                        </div>
                                        <div className="form-group">
                                            <input
                                                type="password"
                                                className="form-input"
                                                name="password"
                                                placeholder="Hasło"
                                                onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                                            />
                                        </div>
                                        <div className="card-button">
                                            <button type="submit">Zaloguj</button>
                                        </div>
                                    </form>
                                    <p className="register-link">Nie masz konta? <a href="/src/containers/Register">Zarejestruj!</a></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Login;
