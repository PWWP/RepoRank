import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import CommentForm from "./parts/repository/CommentForm";
import CommentList from "./parts/repository/CommentList";
import Reaction from "./parts/repository/Reaction";

const RepositoryDetails = ({ token }) => {
    const [repository, setRepository] = useState(null);
    const [error, setError] = useState(null);

    const { repositoryId } = useParams();
    console.log(repositoryId);

    const handleCommentAdded = (newComment) => {
        fetchRepositoryDetails();
    };

    const handleReactionAdded = (reaction) => {
        fetchRepositoryDetails();
    };

    const fetchRepositoryDetails = async () => {
        try {
            try {
                const response = await fetch(`http://localhost:8085/api/repositories/${repositoryId}`);
                const data = await response.json();

                if (response.status === 200) {
                    setRepository(data.data);
                } else if (response.status === 404) {
                    setError('Repozytorium o podanym identyfikatorze nie znalezione');
                } else {
                    setError('Wystąpił błąd podczas pobierania danych o repozytorium');
                }
            } catch (error) {
                console.error('Error fetching repository details:', error);
                setError('Wystąpił błąd podczas pobierania danych o repozytorium');
            }
        } catch (error) {
            console.error('Error fetching repository details:', error);
        }
    };

    useEffect(() => {
        // Pobierz dane repozytorium po zamontowaniu komponentu
        fetchRepositoryDetails();
    }, []);

    return (
        <div className="content-container">
            <div className="container">
                <div>
                    {repository ? (
                        <div className="card" style={{ minWidth: '300px' }}>
                            <div className="card-header">
                                <img src={`/images/img${Math.round(Math.random() * 3) + 1}.jpg`} style={{ maxWidth: '100%', maxHeight: '300px' }} alt="Repository Image" />
                                <h2>{repository.name}</h2>
                                <span style={{ marginBottom: '0px' }}>{repository.comment}</span>
                            </div>
                            <div className="card-body">
                                <div className="date">
                                    <i className="far fa-calendar-alt"></i> Data dodania: {repository.createDate}
                                </div>
                                <div className="counters" style={{ marginBottom: '5px' }}>
                                    <div className="counter">
                                        <i className="fas fa-comments"></i>
                                        <span id="commentsCounter">Komentarze: {repository.comments.length}</span>
                                    </div>
                                    <CommentList comments={repository.comments} />

                                </div>
                                <div>
                                    <CommentForm repoId={repository.id} onCommentAdded={handleCommentAdded} token={token} />
                                </div>
                                <div>
                                    <Reaction repository={repository} onReactionAdded={handleReactionAdded} token={token} />
                                </div>
                            </div>
                        </div>
                    ) : (
                        <p>Ładowanie danych...</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default RepositoryDetails;
