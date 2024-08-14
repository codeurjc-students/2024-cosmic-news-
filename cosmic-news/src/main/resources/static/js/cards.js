let currentFilters = {
    news: "date",
    pictures: "date"
};

const cardWidth = 250 + 2 * 20;

document.addEventListener('DOMContentLoaded', function () {
    load("newsUser", 0);
    load("picturesUser", 0);
    load("pictures", 0, currentFilters.pictures);
    load("news", 0, currentFilters.news);
    load("videos",0);
    load("quizzes",0);
});

function calculateColumns(cards) {
    const containerWidth = cards.offsetWidth;
    const columns = Math.floor(containerWidth / cardWidth);
    return columns;
}

async function checkLoadMore(cardType, page) {
    const loadMoreId = `load${cardType.charAt(0).toUpperCase() + cardType.slice(1)}`;
    const loadMore = document.getElementById(loadMoreId);

    if (!loadMore) return;

    const loadMoreButton = loadMore.querySelector("button");
    loadMoreButton.setAttribute("onclick", `load("${cardType}", ${page}, "${currentFilters[cardType]}")`);
}

async function load(cardType, page, filter) {
    const cards = document.getElementById(cardType);
    const noContent = document.getElementById(`no${cardType.charAt(0).toUpperCase() + cardType.slice(1)}`);
    const loadMore = document.getElementById(`load${cardType.charAt(0).toUpperCase() + cardType.slice(1)}`);

    if (!cards || !noContent || !loadMore) return;

    const amount = Math.max(2, Math.min(5, calculateColumns(cards))); 

    if (filter !== undefined) {
        currentFilters[cardType] = filter;
    }

    const response = await fetch(`/${cardType}/load?page=${page}&size=${amount}&filter=${currentFilters[cardType]}`);
    const newCards = await response.text();

    if (page == 0) {
        cards.innerHTML = newCards;
    } else {
        cards.insertAdjacentHTML("beforeend", newCards);
    }

    if (cards.children.length === 0) {
        noContent.style.display = 'block';
        loadMore.style.display = 'none';
    } else {
        noContent.style.display = 'none';
        loadMore.style.display = 'block';
    }

    checkLoadMore(cardType, page + 1);
}

function setFilter(cardType, filter) {
    currentFilters[cardType] = filter;
    load(cardType, 0, filter);
}