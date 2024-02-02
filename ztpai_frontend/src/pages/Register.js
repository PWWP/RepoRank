import React, { useState, useEffect } from 'react';

const Register = ({ token }) => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
    });

    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    useEffect(() => {
        if (token) {
            setSuccess('Jesteś już zarejestrowany!');
            setTimeout(() => {
                window.location.href = '/';
            }, 5000);
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
            const response = await fetch('http://localhost:8085/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });

            const data = await response.json();

            if (response.status === 200) {
                setSuccess('Użytkownik pomyślnie zarejestrowany. Nastąpi przeniesienie do strony logowania....');
                setTimeout(() => {
                    window.location.href = '/login';
                }, 3000);
            } else if (response.status === 400 && data.message) {
                const errorFields = Object.keys(data.message);
                const errorMessage = errorFields
                    .map((field) => `${field}: ${data.message[field]}`)
                    .join(', ');
                setError(`Użytkownik o takiej nazwie użytkownika lub adresie E-Mail już istnieje: ${errorMessage}`);
                setSuccess('');
            } else {
                const errorData = await response.json();
                setError(errorData.message);
                setSuccess('');
            }
        } catch (error) {
            console.error('Error during registration:', error);
            setError('Wystąpił błąd podczas rejestracji');
            setSuccess('');
        }
    };

    // Jeżeli token nie jest nullem, wyświetl komunikat o zarejestrowaniu użytkownika
    if (token) {
        return (
            <div className="content-container">
                <div className="container">
                    <div className="row">
                        <div className="column-lg-5 column-md-12">
                            <div className="row">
                                <div className="card">
                                    <div className="card-header">Rejestracja</div>
                                    <div className="card-body">
                                        {success && <SuccessMessage message={success} />}
                                        <p className="register-link">Zostaniesz przekierowany na stronę logowania za 5 sekund...</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    // Jeżeli token jest nullem, renderuj standardowy formularz rejestracji
    return (
        <div className="content-container">
            <div className="container">
                <div className="row">
                    <div className="column-lg-5 column-md-12">
                        <div className="row">
                            <div className="card">
                                <div className="card-header">Rejestracja</div>
                                <div className="card-body">
                                    {error && <ErrorMessage message={error} />}
                                    {success && <SuccessMessage message={success} />}
                                    <form method="POST" onSubmit={handleSubmit}>
                                        <div className="form-group">
                                            <input
                                                type="text"
                                                className="form-input"
                                                name="username"
                                                placeholder="Nazwa użytkownika"
                                                onChange={(e) => setFormData({ ...formData, username: e.target.value })}
                                            />
                                        </div>
                                        <div className="form-group">
                                            <input
                                                type="text"
                                                className="form-input"
                                                name="email"
                                                placeholder="E-Mail"
                                                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
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
                                            <button type="submit">Zarejestruj</button>
                                        </div>
                                    </form>
                                    <p className="register-link">
                                        Masz już konto? <a href="/src/containers/Login">Zaloguj się!</a>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Register;
