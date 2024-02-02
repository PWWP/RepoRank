import React, { useEffect, useState } from 'react';

const Toplist = () => {
    const [repoList, setRepoList] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch('http://localhost:8085/api/repositories/list');
                const data = await response.json();

                if (data && data.data) {
                    setRepoList(data.data);
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
                        <div className="card">
                            <div className="card-header">
                                <h2>Ranking Repozytoriów GitHub</h2>
                            </div>
                            <div className="card-body">
                                <table className="ranking-table">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Nazwa repozytorium</th>
                                        <th>Ilość reakcji</th>
                                        <th>Ilość komentarzy</th>
                                        <th>Data dodania</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {repoList.map((repo, index) => (
                                        <tr key={repo.name} onClick={() => window.location.href = `/repo/${repo.id}`}>
                                            <td>{index + 1}</td>
                                            <td>{repo.name}</td>
                                            <td>{repo.reactions.length}</td>
                                            <td>{repo.comments.length}</td>
                                            <td>{repo.createDate}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Toplist;
