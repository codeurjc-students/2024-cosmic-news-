// Global variable to store the current filters
let currentFilters = {
    news: "date",
    pictures: "date"
};

const cardWidth = 250 + 2 * 20; // Maximum width of each offer + margins (edges are included in the a)

document.addEventListener('DOMContentLoaded', function () {
    load("newsUser", 0);
    load("picturesUser", 0);
    load("pictures", 0, currentFilters.pictures);
    load("news", 0, currentFilters.news);
});

function calculateColumns(cards) {
    const containerWidth = cards.offsetWidth;
    const columns = Math.floor(containerWidth / cardWidth);
    return columns;
}

async function checkLoadMore(cardType, page) {
    const loadMoreId = `load${cardType.charAt(0).toUpperCase() + cardType.slice(1)}`;
    const loadMore = document.getElementById(loadMoreId);

    if (!loadMore) return; // If the load more button container is not found, exit the function

    const loadMoreButton = loadMore.querySelector("button");
    loadMoreButton.setAttribute("onclick", `load("${cardType}", ${page}, "${currentFilters[cardType]}")`);
}

async function load(cardType, page, filter) {
    const cards = document.getElementById(cardType);
    const noContent = document.getElementById(`no${cardType.charAt(0).toUpperCase() + cardType.slice(1)}`);
    const loadMore = document.getElementById(`load${cardType.charAt(0).toUpperCase() + cardType.slice(1)}`);

    if (!cards || !noContent || !loadMore) return; // If the cards container or no-content message is not found, exit the function

    const amount = Math.max(2, Math.min(5, calculateColumns(cards)));  // Between 2 and 5 cards

    // Update global filter if one is provided
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

    // Show or hide the "No content" message and load more button
    if (cards.children.length === 0) {
        noContent.style.display = 'block';
        loadMore.style.display = 'none';
    } else {
        noContent.style.display = 'none';
        loadMore.style.display = 'block';
    }

    checkLoadMore(cardType, page + 1);  // Prepare for next page
}

// Function to update the current filter and reload the content
function setFilter(cardType, filter) {
    currentFilters[cardType] = filter; // Update the global filter
    load(cardType, 0, filter); // Reload the content with the selected filter
}