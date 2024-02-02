import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const Logout = ({ onLogout }) => {
    const navigate = useNavigate();

    useEffect(() => {
        onLogout();

        const timeoutId = setTimeout(() => {
            navigate('/');
        }, 5000);

        return () => clearTimeout(timeoutId);
    }, [onLogout, navigate]);

    return (
        <div className="content-container">
            <div className="container">
                <div className="row">
                    <div className="column-lg-6 column-md-12">
                        <div className="card">
                            <div className="card-header">
                                Wylogowano pomyślnie
                            </div>
                            <div className="card-body">
                                <p>Zostałeś pomyślnie wylogowany.</p>
                                <p>Przekierowanie na stronę główną nastąpi za 5 sekund...</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Logout;
