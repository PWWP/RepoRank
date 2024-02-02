import React from 'react';

const CommentList = ({ comments }) => {
    return (
        <div className="comments" style={{ width: '100%' }}>
            <ul className="list-group">
                {comments.map(comment => (
                    <li key={comment.id} className="list-group-item">
                        <p>{comment.content}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default CommentList;
