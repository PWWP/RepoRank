import React, { useState } from 'react';

// Komponent do wyświetlania komunikatu błędu
const ErrorMessage = ({ message }) => {
    return (
        <div className="alert alert-danger mt-2 mb-2" role="alert">
            {message}
        </div>
    );
};

// Komponent do wyświetlania komunikatu sukcesu
const SuccessMessage = ({ message }) => {
    return (
        <div className="alert alert-success mt-2 mb-2" role="alert">
            {message}
        </div>
    );
};

const CommentForm = ({ repoId, onCommentAdded, token }) => {
    const [content, setContent] = useState('');
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    if(!token){
        return "";
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            // Wysyłanie komentarza do API
            const response = await fetch(`http://localhost:8085/api/comments/add?repoId=${repoId}&content=${content}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
            });

            const result = await response.json();

            if (response.ok) {
                // Jeśli odpowiedź jest OK (status 200)
                setSuccess(result.message);

                // Zaktualizuj stan komponentu nadrzędnego, jeśli komentarz został pomyślnie dodany
                if (result.statusCode === 200) {
                    onCommentAdded(result.message);
                }
            } else {
                // Jeśli odpowiedź nie jest OK, obsłuż błędy
                setError(result.error);

                // Zaktualizuj stan komponentu nadrzędnego, jeśli jest błąd (np. brak autoryzacji)
                if (result.statusCode === 401 || result.statusCode === 404) {
                    setSuccess(null); // Wyczyść komunikat sukcesu w przypadku błędu
                }
            }

            // Wyczyść zawartość pola komentarza
            setContent('');
        } catch (error) {
            console.error('Error submitting comment:', error);
            setError('Wystąpił błąd podczas wysyłania komentarza.');
        }
    };

    return (
        <div>
            {error && <ErrorMessage message={error} />}
            {success && <SuccessMessage message={success} />}
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="commentContent" className="form-label">Dodaj komentarz:</label>
                    <textarea
                        className="form-control"
                        id="commentContent"
                        name="commentContent"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" className="btn btn-primary">Dodaj komentarz</button>
            </form>
        </div>
    );
};

export default CommentForm;
