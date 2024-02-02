import React from 'react';
import { Link } from 'react-router-dom';
import Addrepo from "../pages/Addrepo";

const Topbar = ({ token }) => {
    return (
        <div className="navbar desktop-nav" style={{maxWidth: 1200, margin: '0 auto'}}>
            <div className="logo" onClick={() => window.location.href = '/'}>
                <i className="fas fa-chart-simple"></i>
                <span>RepoRank</span>
            </div>
            <div className="nav-buttons">
                <a href="/src/containers/Toplist"><i className="fas fa-chart-column"></i></a>
                <a href="#" id="searchButton"><i className="fas fa-search"></i></a>
                <div className="search-input">
                    <input type="text" id="searchInput" placeholder="Wyszukaj" />
                </div>

                <a><a className="fas fa-bell"></a></a>
                {token ? (
                    <>
                        <a href="/src/containers/Userinfo"><i className="fas fa-user"></i></a>
                        <a href="/src/containers/Logout"><i className="fas fa-sign-out-alt"></i></a>
                    </>
                ) : (
                    <a href="/src/containers/Login"><i className="fas fa-sign-in-alt"></i> Login</a>
                )}

                <div className="add-repo-button">
                    <a href="/src/containers/Addrepo">DODAJ REPO <i style={{paddingLeft: 10}} className="fas fa-plus"></i></a>
                </div>
            </div>
        </div>
    );
};

export default Topbar;
