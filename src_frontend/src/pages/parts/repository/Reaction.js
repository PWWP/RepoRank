import React, {useState} from 'react';

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

const Reaction = ({ repository, token, onReactionAdded, disableMessage}) => {

    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const handleLike = async () => {
        try {
            const response = await fetch(`http://localhost:8085/api/reactions/add?repoId=${repository.id}&reactionType=LIKE`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}` // Dodaj swój token autoryzacyjny
                },
            });

            if (response.ok) {
                // Jeśli odpowiedź jest OK, zaktualizuj stan polubień i odlubień
                onReactionAdded("LIKE");
                if(!disableMessage) {
                    setSuccess("Polubiono wybrane repozytorium");
                }
            } else {
                const errorData = await response.json();
                if(!disableMessage) {
                    setError(errorData.error);
                }
            }
        } catch (error) {
            if(!disableMessage) {
                setError('Error adding like:', error);
            }
        }
    };

    const handleDislike = async () => {
        try {
            const response = await fetch(`http://localhost:8085/api/reactions/add?repoId=${repository.id}&reactionType=DISLIKE`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}` // Dodaj swój token autoryzacyjny
                },
            });

            if (response.ok) {
                // Jeśli odpowiedź jest OK, zaktualizuj stan polubień i odlubień
                onReactionAdded("DISLIKE");
                if(!disableMessage) {
                    setSuccess("Dodano negatywną ocenę dla repozytorium");
                }
            } else {
                const errorData = await response.json();
                if(!disableMessage) {
                    setError(errorData.error);
                }
            }
        } catch (error) {
            if(!disableMessage) {
                setError('Error adding dislike:', error);
            }
        }
    };

    return (
        <div className="button-container">
            <div className="mb-1">
                {error && <ErrorMessage message={error} />}
                {success && <SuccessMessage message={success} />}
            </div>
            <div className="counters">
                <div className="counter">
                    <i className="fas fa-heart" onClick={handleLike}></i>
                    <span id={`like-counter-${repository.id}`}>{repository.likes}</span>
                </div>
                <div className="counter">
                    <i className="fa-solid fa-square-minus" onClick={handleDislike}></i>
                    <span id={`unlike-counter-${repository.id}`}>{repository.unlikes}</span>
                </div>
            </div>
        </div>
    );
};

export default Reaction;
