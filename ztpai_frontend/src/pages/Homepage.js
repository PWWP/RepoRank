import React, { useEffect, useState } from 'react';
import Reaction from "./parts/repository/Reaction";


const HomePage = ({ token }) => {
    const [repoList, setRepoList] = useState([]);



    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch('http://localhost:8085/api/repositories/list');
                const data = await response.json();

                if (data && data.data) {
                    setRepoList(data.data.slice(0, 5));
                }
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);

    return (
        <div className="content-container">
            <div className="container">
                <div className="row">
                    <div className="column-lg-12 column-md-12">
                        <h1>Ranking repozytoriów GitHub</h1>
                        <h2>Wybrane dla Ciebie:</h2>
                    </div>
                </div>
                <div className="row">
                    <div className="carousel">
                        {repoList.map((repo) => (
                            <div key={repo.repositoryId} className="card" style={{ minWidth: '300px' }}>
                                <div className="card-header">
                                    <img
                                        src={`/images/img${Math.round(Math.random() * 3) + 1}.jpg`}
                                        style={{ maxWidth: '100%', minHeight: '230px' }}
                                        alt="Repository"
                                    />
                                    <h2>{repo.name}</h2>
                                    <span style={{ marginBottom: '0px' }}>{repo.comment}</span>
                                </div>
                                <div className="card-body">
                                    <div className="date">
                                        <i className="far fa-calendar-alt"></i> Data dodania: {new Date(repo.addTime * 1000).toLocaleDateString()}
                                    </div>
                                    <div className="counters" style={{ marginBottom: '5px' }}>
                                        <div className="counter">
                                            <i className="fas fa-comments"></i>
                                            <span id={`commentsCounter-${repo.repositoryId}`}>Komentarze: {repo.comments.length}</span>
                                        </div>
                                    </div>
                                    {repo.authors.map((author) => (
                                        <div key={author.authorId} className="author" style={{ marginTop: '10px' }}>
                                            <i className="fas fa-user"></i> {author.username}
                                            <span style={{ marginLeft: '10px' }} className="contribution">{author.contribution}% </span>
                                        </div>
                                    ))}
                                    <div className="button-container">
                                        <button
                                            className="more-button"
                                            onClick={() => window.location.href = `/repo/${repo.id}`} >
                                            Zobacz więcej
                                        </button>
                                        <Reaction disableMessage={true} repository={repo} onClick={() => window.location.href = `/repo/${repo.id}`} token={token} />
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
                <div className="row mt-5">
                    <div className="col-lg-5 col-md-12">
                        <div
                            className="card clickable-card"
                            onClick={() => (window.location.href = '/register')}
                        >
                            <div className="card-header">
                                <h3>CAŁKOWICIE DARMOWE</h3>
                            </div>
                            <div className="card-body">
                                Korzystanie z aplikacji REPORANK jest całkowicie darmowe, wymaga jedynie
                                rejestracji w celu potwierdzenia unikalności głosów.
                            </div>
                        </div>
                    </div>

                    <div className="col-lg-2 col-md-12"></div>

                    <div className="col-lg-5 col-md-12">
                        <div
                            className="card clickable-card"
                            onClick={() => (window.location.href = '/addrepo')}
                        >
                            <div className="card-header">
                                <h3>DODAJ REPOZYTORIUM DO BAZY</h3>
                            </div>
                            <div className="card-body">
                                W łatwych 3 kliknięciach można dodać repozytorium do naszej bazy. Po
                                dodaniu już tylko można się cieszyć możliwością oceny i promowania danego
                                repozytorium!
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default HomePage;
