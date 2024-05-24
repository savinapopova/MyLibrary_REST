class ReviewModel {
    id: number;
    userEmail: string;
    reviewDescription?: string;
    rating: number;
    bookId: number;
    date: string;


    constructor(id: number, userEmail: string, reviewDescription: string,
                rating: number, bookId: number, date: string) {
        this.id = id;
        this.userEmail = userEmail;
        this.reviewDescription = reviewDescription;
        this.rating = rating;
        this.bookId = bookId;
        this.date = date;
    }
}

export default ReviewModel;