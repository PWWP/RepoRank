import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Addrepo = ({ token }) => {

    const [successMessage, setSuccessMessage] = useState('');
    const [formData, setFormData] = useState({
        name: '',
        comment: '',
        url: '',
        author: '',
        image: null,
    });

    const navigate = useNavigate();
    const [authorSuggestions, setAuthorSuggestions] = useState([]);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });

        if (name === 'author') {
            fetchAuthorSuggestions(value);
        }
    };

    const fetchAuthorSuggestions = (nicknameContains) => {
        fetch(`/api_getauthors/${nicknameContains}`)
            .then(response => response.json())
            .then(data => {
                setAuthorSuggestions(data);
            })
            .catch(error => console.error('Error fetching author suggestions:', error));
    };

    const handleAuthorSuggestionClick = (selectedAuthor) => {
        setFormData({
            ...formData,
            author: selectedAuthor,
        });

        setAuthorSuggestions([]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const author = createAuthor(formData.author);

            const repositoryData = {
                id: '3fa85f64-5717-4562-b3fc-2c963f66afa6',
                name: formData.name,
                comment: formData.comment,
                url: formData.url,
                image: formData.image,
                addTime: 0,
                createdBy: null,
                reactions: [],
                comments: [],
                authors: [author],
            };

            // Symulacja wysyłania danych do API
            console.log('Form submitted with data:', repositoryData);

            const response = await fetch('http://localhost:8085/api/repositories/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(repositoryData),
            });

            const result = await response.json();
            console.log('API response:', result);

            setSuccessMessage('Pomyślnie dodano nowe repozytorium!');

            const repoId = result.data.id;
            navigate(`/repo/${repoId}`);
        } catch (error) {
            console.error('Error submitting form:', error);
        }
    };

    const createAuthor = (name) => {
        return {
            id: '3fa85f64-5717-4562-b3fc-2c963f66afa6',
            username: name,
            name: name,
            surname: name,
            externalProfileUrl: 'string',
            contribution: Math.floor(Math.random() * 100),
        };
    };


    return (
        <div className="content-container">
            <div className="container">
                <div className="row">
                    <div className="column-md-5 column-lg-5">
                        <div className="card">
                            <div className="card-title">
                                <h2 style={{ color: '#78AE94' }}>Dodaj nowe repozytorium</h2>
                            </div>
                            <div className="card-body">
                                <form id="addRepoForm" onSubmit={handleSubmit}>
                                    <table className="table" style={{ padding: '10px' }}>
                                        <tr>
                                            <td>
                                                <label htmlFor="name" className="form-label">Nazwa</label>
                                            </td>
                                            <td>
                                                <input type="text" className="form-control" id="name" name="name" required onChange={handleInputChange} />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label htmlFor="comment" className="form-label">Komentarz</label>
                                            </td>
                                            <td>
                                                <textarea className="form-control" id="comment" name="comment" rows="3" onChange={handleInputChange}></textarea>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label htmlFor="url" className="form-label">URL</label>
                                            </td>
                                            <td>
                                                <input type="text" className="form-control" id="url" name="url" onChange={handleInputChange} />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label htmlFor="author" className="form-label">Autor</label>
                                            </td>
                                            <td>
                                                <input type="text" className="form-control" id="author" name="author" required onChange={handleInputChange} />
                                                <div id="authorSuggestions" className="dropdown-menu" aria-labelledby="author">
                                                    {authorSuggestions.map((author, index) => (
                                                        <div key={index} className="dropdown-item" onClick={() => handleAuthorSuggestionClick(author)}>
                                                            {author}
                                                        </div>
                                                    ))}
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label htmlFor="image" className="form-label">Obraz</label>
                                            </td>
                                            <td>
                                                <input type="file" className="form-control" id="image" name="image" onChange={handleInputChange} />
                                            </td>
                                        </tr>
                                    </table>

                                    <div className="card-button">
                                        <button type="submit" className="btn btn-primary">Dodaj</button>
                                    </div>
                                </form>
                                {successMessage && (
                                    <div className="alert alert-success mt-3" role="alert">
                                        {successMessage}
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                    <div className="column-md-2 column-lg-2"></div>
                    <div className="column-md-5 column-lg-5">
                        <div className="card">
                            <div className="card-title">
                                <h2 style={{ color: '#78AE94' }}>Dodaj repo automatycznie po URL</h2>
                            </div>
                            <div className="card-body">
                                <form id="addRepoByUrlForm" onSubmit={handleSubmit}>
                                    <div className="mb-4">
                                        <label htmlFor="url" className="form-label">Link: </label>
                                        <input type="text" className="form-control" id="url" name="url" required onChange={handleInputChange} />
                                    </div>

                                    <div className="card-button">
                                        <button type="submit" className="btn btn-primary">Dodaj</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Addrepo;
